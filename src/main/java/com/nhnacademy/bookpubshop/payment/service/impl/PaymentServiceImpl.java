package com.nhnacademy.bookpubshop.payment.service.impl;

import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.event.PaymentEvent;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepository;
import com.nhnacademy.bookpubshop.payment.service.PaymentService;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.exception.NotFoundPaymentStateException;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
import com.nhnacademy.bookpubshop.state.PaymentState;
import com.nhnacademy.bookpubshop.state.PaymentTypeState;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 결제 서비스 구현체 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@EnableAsync
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStateCodeRepository stateRepository;
    private final PaymentTypeRepository typeRepository;
    private final TossAdaptor tossAdaptor;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Boolean verifyPayment(String orderId, Long amount) {
        GetOrderVerifyResponseDto getOrderVerifyResponseDto
                = orderRepository.verifyPayment(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return Objects.equals(getOrderVerifyResponseDto.getAmount(), amount);
    }

    @Override
    @Transactional
    public void createPayment(String paymentKey, String orderId, Long amount) {
        TossResponseDto tossResponseDto = tossAdaptor.requestPayment(paymentKey, orderId, amount);

        BookpubOrder bookpubOrder = getBookpubOrder(orderId);
        PaymentStateCode paymentStateCode = getPaymentStateCode();
        PaymentTypeStateCode paymentTypeStateCode = getPaymentTypeStateCode();

        String approvedAt = tossResponseDto.getApprovedAt().split("\\+")[0];

        Payment payment = paymentRepository.save(Payment.builder()
                .order(bookpubOrder)
                .paymentStateCode(paymentStateCode)
                .paymentTypeStateCode(paymentTypeStateCode)
                .approvedAt(LocalDateTime.parse(approvedAt))
                .paymentKey(tossResponseDto.getPaymentKey())
                .receipt(tossResponseDto.getReceipt().getUrl())
                .build());

        eventPublisher.publishEvent(new PaymentEvent(bookpubOrder, payment, tossResponseDto));
    }

    /**
     * 결제 한 주문을 가져오는 메소드.
     *
     * @param orderId 주문 id
     * @return 주문
     */
    private BookpubOrder getBookpubOrder(String orderId) {
        return orderRepository.getOrderByOrderKey(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    /**
     * 결제상태를 가져오는 메소드.
     *
     * @return 결제상태.
     */
    private PaymentStateCode getPaymentStateCode() {
        return stateRepository.getPaymentStateCode(PaymentState.COMPLETE_PAYMENT.getName())
                .orElseThrow(NotFoundPaymentStateException::new);
    }

    /**
     * 결제 유형 상태코드를 가져오는 메소드.
     *
     * @return 결제상태유형코드.
     */
    private PaymentTypeStateCode getPaymentTypeStateCode() {
        return typeRepository.getPaymentType(PaymentTypeState.CARD.getName())
                .orElseThrow(NotFoundPaymentStateException::new);
    }


}
