package com.challang.backend.withdrawal.repository;

import com.challang.backend.withdrawal.entity.WithdrawalReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalReasonRepository extends JpaRepository<WithdrawalReason, Long> {
}