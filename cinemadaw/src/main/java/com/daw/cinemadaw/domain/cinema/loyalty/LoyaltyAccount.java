package com.daw.cinemadaw.domain.cinema.loyalty;

import java.time.LocalDateTime;

import com.daw.cinemadaw.domain.cinema.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loyalty_accounts")
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private int points = 0;

    @Column(name = "total_spent", nullable = false)
    private double totalSpent = 0.0;

    @Column(name = "available_discount", nullable = false)
    private double availableDiscount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoyaltyTier tier = LoyaltyTier.BRONZE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public LoyaltyAccount() {
    }

    public LoyaltyAccount(User user) {
        this.user = user;
        this.points = 0;
        this.totalSpent = 0.0;
        this.tier = LoyaltyTier.BRONZE;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public double getAvailableDiscount() {
        return availableDiscount;
    }

    public void setAvailableDiscount(double availableDiscount) {
        this.availableDiscount = availableDiscount;
    }

    public LoyaltyTier getTier() {
        return tier;
    }

    public void setTier(LoyaltyTier tier) {
        this.tier = tier;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
