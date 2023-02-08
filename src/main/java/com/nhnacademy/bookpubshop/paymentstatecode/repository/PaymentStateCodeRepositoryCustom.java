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
    /**
     * 모든 결제상태를 불러오는 메소드.
     *
     * @return 모든 결제상태.
     */
    List<GetPaymentStateResponseDto> getAllPaymentState();

    /**
     * 상태이름으로 결제상태를 불러오는 메소드.
     *
     * @param state 결제상태명.
     * @return 결제상태.
     */
    Optional<PaymentStateCode> getPaymentStateCode(String state);
}
