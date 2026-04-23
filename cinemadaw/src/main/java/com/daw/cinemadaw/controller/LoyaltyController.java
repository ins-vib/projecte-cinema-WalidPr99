package com.daw.cinemadaw.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyAccount;
import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyTier;
import com.daw.cinemadaw.domain.cinema.loyalty.PointTransaction;
import com.daw.cinemadaw.domain.cinema.user.Role;
import com.daw.cinemadaw.service.CustomUserDetails;
import com.daw.cinemadaw.service.LoyaltyService;

@Controller
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @GetMapping("/loyalty")
    public String panel(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        LoyaltyAccount account = loyaltyService.getOrCreateAccount(user.getUsername());
        double amountToNext = loyaltyService.amountToNextTier(account);
        LoyaltyTier nextTier = loyaltyService.nextTier(account.getTier());

        int redeemableBlocks = account.getPoints() / LoyaltyService.REDEEM_BLOCK_POINTS;
        double maxDiscount = redeemableBlocks * LoyaltyService.REDEEM_BLOCK_DISCOUNT;
        double totalRedeemed = loyaltyService.getTotalRedeemedDiscount(user.getUsername());

        model.addAttribute("account", account);
        model.addAttribute("totalRedeemed", totalRedeemed);
        model.addAttribute("amountToNext", amountToNext);
        model.addAttribute("nextTier", nextTier);
        model.addAttribute("redeemBlockPoints", LoyaltyService.REDEEM_BLOCK_POINTS);
        model.addAttribute("redeemBlockDiscount", LoyaltyService.REDEEM_BLOCK_DISCOUNT);
        model.addAttribute("pointsPerEuro", LoyaltyService.POINTS_PER_EURO);
        model.addAttribute("silverThreshold", LoyaltyService.SILVER_THRESHOLD);
        model.addAttribute("goldThreshold", LoyaltyService.GOLD_THRESHOLD);
        model.addAttribute("maxDiscount", maxDiscount);
        model.addAttribute("redeemableBlocks", redeemableBlocks);

        return "loyalty/panel";
    }

    @GetMapping("/loyalty/redeem")
    public String redeemForm(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        LoyaltyAccount account = loyaltyService.getOrCreateAccount(user.getUsername());
        int redeemableBlocks = account.getPoints() / LoyaltyService.REDEEM_BLOCK_POINTS;

        model.addAttribute("account", account);
        model.addAttribute("redeemableBlocks", redeemableBlocks);
        model.addAttribute("redeemBlockPoints", LoyaltyService.REDEEM_BLOCK_POINTS);
        model.addAttribute("redeemBlockDiscount", LoyaltyService.REDEEM_BLOCK_DISCOUNT);

        return "loyalty/redeem";
    }

    @PostMapping("/loyalty/redeem")
    public String redeem(@RequestParam("points") int points,
                         @AuthenticationPrincipal CustomUserDetails user,
                         RedirectAttributes redirectAttributes) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        try {
            loyaltyService.redeemPoints(user.getUsername(), points);
            double discount = (points / (double) LoyaltyService.REDEEM_BLOCK_POINTS) * LoyaltyService.REDEEM_BLOCK_DISCOUNT;
            redirectAttributes.addFlashAttribute("successMessage",
                    "Has canviat " + points + " punts per " + String.format("%.2f", discount) + " € de descompte.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/loyalty/redeem";
        }

        return "redirect:/loyalty";
    }

    @GetMapping("/loyalty/history")
    public String history(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        LoyaltyAccount account = loyaltyService.getOrCreateAccount(user.getUsername());
        List<PointTransaction> transactions = loyaltyService.getHistory(user.getUsername());

        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);

        return "loyalty/history";
    }

    private boolean isClient(CustomUserDetails user) {
        return user != null
                && user.getUser() != null
                && Role.CLIENT.equals(user.getUser().getRole());
    }
}
