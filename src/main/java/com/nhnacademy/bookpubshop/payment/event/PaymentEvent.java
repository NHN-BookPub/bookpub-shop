package com.nhnacademy.bookpubshop.payment.event;

import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 이벤트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class PaymentEvent {
    private BookpubOrder order;
    private Payment payment;
    private TossResponseDto tossResponseDto;
}
