package com.nhnacademy.bookpubshop.paymentstatecode.repository;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 결제생태 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PaymentStateCodeRepositoryCustom {
    List<GetPaymentStateResponseDto> getAllPaymentState();

    Optional<PaymentStateCode> getPaymentStateCode(String state);
}
