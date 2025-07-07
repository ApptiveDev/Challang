package com.challang.backend.withdrawal.service;

import com.challang.backend.withdrawal.dto.WithdrawalReasonResponse;
import com.challang.backend.withdrawal.repository.WithdrawalReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawalReasonService {

    private final WithdrawalReasonRepository withdrawalReasonRepository;

    public List<WithdrawalReasonResponse> getAllWithdrawalReasons() {
        return withdrawalReasonRepository.findAll().stream()
                .map(WithdrawalReasonResponse::from)
                .collect(Collectors.toList());
    }
}