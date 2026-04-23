package com.daw.cinemadaw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyAccount;
import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyTier;
import com.daw.cinemadaw.domain.cinema.loyalty.PointTransaction;
import com.daw.cinemadaw.domain.cinema.loyalty.TransactionType;
import com.daw.cinemadaw.domain.cinema.user.User;
import com.daw.cinemadaw.repository.ComandaRepository;
import com.daw.cinemadaw.repository.LoyaltyAccountRepository;
import com.daw.cinemadaw.repository.PointTransactionRepository;
import com.daw.cinemadaw.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class LoyaltyService {

    public static final int POINTS_PER_EURO = 1;

    public static final int REDEEM_BLOCK_POINTS = 100;
    public static final double REDEEM_BLOCK_DISCOUNT = 5.0;

    public static final double SILVER_THRESHOLD = 100.0;
    public static final double GOLD_THRESHOLD = 500.0;

    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final UserRepository userRepository;
    private final ComandaRepository comandaRepository;

    public LoyaltyService(LoyaltyAccountRepository loyaltyAccountRepository,
                          PointTransactionRepository pointTransactionRepository,
                          UserRepository userRepository,
                          ComandaRepository comandaRepository) {
        this.loyaltyAccountRepository = loyaltyAccountRepository;
        this.pointTransactionRepository = pointTransactionRepository;
        this.userRepository = userRepository;
        this.comandaRepository = comandaRepository;
    }

    @Transactional
    public LoyaltyAccount getOrCreateAccount(String username) {
        return loyaltyAccountRepository.findByUser_Username(username)
                .orElseGet(() -> {
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat: " + username));
                    LoyaltyAccount account = new LoyaltyAccount(user);
                    return loyaltyAccountRepository.save(account);
                });
    }

    @Transactional
    public PointTransaction earnPointsForOrder(String username, Comanda comanda) {
        if (comanda == null || comanda.getTotalAmount() <= 0) {
            return null;
        }

        LoyaltyAccount account = getOrCreateAccount(username);

        int pointsEarned = (int) Math.floor(comanda.getTotalAmount() * POINTS_PER_EURO);

        account.setPoints(account.getPoints() + pointsEarned);
        account.setTotalSpent(account.getTotalSpent() + comanda.getTotalAmount());
        account.setTier(calculateTier(account.getTotalSpent()));
        loyaltyAccountRepository.save(account);

        PointTransaction tx = new PointTransaction(
                account,
                TransactionType.EARN,
                pointsEarned,
                "Punts guanyats per la compra #" + comanda.getId(),
                comanda);
        return pointTransactionRepository.save(tx);
    }

    @Transactional
    public PointTransaction redeemPoints(String username, int pointsToRedeem) {
        if (pointsToRedeem <= 0 || pointsToRedeem % REDEEM_BLOCK_POINTS != 0) {
            throw new IllegalArgumentException("Has de canviar múltiples de " + REDEEM_BLOCK_POINTS + " punts.");
        }

        LoyaltyAccount account = getOrCreateAccount(username);

        if (account.getPoints() < pointsToRedeem) {
            throw new IllegalStateException("No tens prou punts. Tens " + account.getPoints() + " punts.");
        }

        double discount = (pointsToRedeem / (double) REDEEM_BLOCK_POINTS) * REDEEM_BLOCK_DISCOUNT;

        account.setPoints(account.getPoints() - pointsToRedeem);
        account.setAvailableDiscount(account.getAvailableDiscount() + discount);
        loyaltyAccountRepository.save(account);

        PointTransaction tx = new PointTransaction(
                account,
                TransactionType.REDEEM,
                -pointsToRedeem,
                "Canvi de " + pointsToRedeem + " punts per " + String.format("%.2f", discount) + " € de descompte",
                null);
        return pointTransactionRepository.save(tx);
    }

    @Transactional
    public double applyDiscountToOrder(String username, Comanda comanda) {
        if (comanda == null) {
            return 0.0;
        }
        LoyaltyAccount account = getOrCreateAccount(username);
        double credit = account.getAvailableDiscount();
        double subtotal = comanda.getTotalAmount();
        if (credit <= 0 || subtotal <= 0) {
            return 0.0;
        }
        double applied = Math.min(credit, subtotal);
        account.setAvailableDiscount(credit - applied);
        loyaltyAccountRepository.save(account);

        comanda.setTotalAmount(Math.max(0, subtotal - applied));
        comandaRepository.save(comanda);

        PointTransaction tx = new PointTransaction(
                account,
                TransactionType.REDEEM,
                0,
                "Descompte de " + String.format("%.2f", applied) + " € aplicat a la compra #" + comanda.getId(),
                comanda);
        pointTransactionRepository.save(tx);

        return applied;
    }

    public double getAvailableDiscount(String username) {
        return getOrCreateAccount(username).getAvailableDiscount();
    }

    public List<PointTransaction> getHistory(String username) {
        LoyaltyAccount account = getOrCreateAccount(username);
        return pointTransactionRepository.findByLoyaltyAccountOrderByCreatedAtDesc(account);
    }

    public double getTotalRedeemedDiscount(String username) {
        LoyaltyAccount account = getOrCreateAccount(username);
        List<PointTransaction> all = pointTransactionRepository.findByLoyaltyAccountOrderByCreatedAtDesc(account);
        int redeemedPoints = 0;
        for (PointTransaction tx : all) {
            if (tx.getType() == TransactionType.REDEEM) {
                redeemedPoints += Math.abs(tx.getPoints());
            }
        }
        return (redeemedPoints / (double) REDEEM_BLOCK_POINTS) * REDEEM_BLOCK_DISCOUNT;
    }

    public LoyaltyTier calculateTier(double totalSpent) {
        if (totalSpent >= GOLD_THRESHOLD) {
            return LoyaltyTier.GOLD;
        }
        if (totalSpent >= SILVER_THRESHOLD) {
            return LoyaltyTier.SILVER;
        }
        return LoyaltyTier.BRONZE;
    }

    public double amountToNextTier(LoyaltyAccount account) {
        double spent = account.getTotalSpent();
        if (spent < SILVER_THRESHOLD) {
            return SILVER_THRESHOLD - spent;
        }
        if (spent < GOLD_THRESHOLD) {
            return GOLD_THRESHOLD - spent;
        }
        return 0.0;
    }

    public LoyaltyTier nextTier(LoyaltyTier current) {
        if (current == LoyaltyTier.BRONZE) return LoyaltyTier.SILVER;
        if (current == LoyaltyTier.SILVER) return LoyaltyTier.GOLD;
        return LoyaltyTier.GOLD;
    }
}
