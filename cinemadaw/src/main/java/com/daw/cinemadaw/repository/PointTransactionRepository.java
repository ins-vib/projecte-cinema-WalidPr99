package com.daw.cinemadaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.cinemadaw.domain.cinema.loyalty.LoyaltyAccount;
import com.daw.cinemadaw.domain.cinema.loyalty.PointTransaction;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    List<PointTransaction> findByLoyaltyAccountOrderByCreatedAtDesc(LoyaltyAccount account);
}
