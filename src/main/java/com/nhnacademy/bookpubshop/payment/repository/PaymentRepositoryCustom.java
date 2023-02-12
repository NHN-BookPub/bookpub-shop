package com.nhnacademy.bookpubshop.payment.repository;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.response.GetRefundResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 결제 레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PaymentRepositoryCustom {
    Optional<GetRefundResponseDto> getRefundInfo(
            RefundRequestDto refundRequestDto);

    Optional<Payment> getPayment(String paymentKey);

    Optional<BookpubOrder> getOrderByPaymentKey(String paymentKey);
}
