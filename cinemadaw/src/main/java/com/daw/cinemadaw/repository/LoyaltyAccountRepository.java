package com.daw.cinemadaw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyAccount;
import com.daw.cinemadaw.domain.cinema.user.User;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, Long> {

    Optional<LoyaltyAccount> findByUser(User user);

    Optional<LoyaltyAccount> findByUser_Username(String username);
}
