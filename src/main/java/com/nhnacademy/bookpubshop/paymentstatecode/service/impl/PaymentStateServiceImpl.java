package com.nhnacademy.bookpubshop.paymentstatecode.service.impl;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.exception.NotFoundPaymentStateException;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymentstatecode.service.PaymentStateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 결제상태 서비스 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class PaymentStateServiceImpl implements PaymentStateService {
    private final PaymentStateCodeRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPaymentStateResponseDto> getAllPaymentState() {
        return repository.getAllPaymentState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentStateCode getPaymentState(String state) {
        return repository.getPaymentStateCode(state)
                .orElseThrow(NotFoundPaymentStateException::new);
    }
}
