package com.nhnacademy.bookpubshop.payment.event.listener;

import com.nhnacademy.bookpubshop.card.entity.Card;
import com.nhnacademy.bookpubshop.card.repository.CardRepository;
import com.nhnacademy.bookpubshop.delivery.adaptor.DeliveryAdaptor;
import com.nhnacademy.bookpubshop.delivery.dto.request.CreateDeliveryRequestDto;
import com.nhnacademy.bookpubshop.delivery.dto.response.CreateDeliveryResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductStateException;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.orderstatecode.exception.NotFoundOrderStateException;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.event.PaymentEvent;
import com.nhnacademy.bookpubshop.state.CardCompany;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 결제 이벤트가 발생하면 이벤트를 실행시키는 클래스 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductStateCodeRepository orderProductStateRepository;
    private final OrderStateCodeRepository orderStateCodeRepository;
    private final CardRepository cardRepository;
    private final DeliveryAdaptor deliveryAdaptor;

    /**
     * 결제 이벤트 발생시 주문 상태변경, 카드 db저장, 주문에 송장번호가 업데이트 되는 메소드입니다.
     * db정보가 업데이트 될 동안 결제완료가 기다려야 할 필요가 없으니 비동기로 구성합니다.
     *
     * @param paymentEvent 결제 이벤트.
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = PaymentEvent.class)
    public void handle(PaymentEvent paymentEvent) {
        BookpubOrder order = paymentEvent.getOrder();
        TossResponseDto tossResponseDto = paymentEvent.getTossResponseDto();
        Payment payment = paymentEvent.getPayment();

        changeOrderStateToWaitDelivery(order);
        CreateDeliveryResponseDto invoiceNumber = sendDelivery(order);
        order.modifyInvoiceNo(invoiceNumber.getInvoiceNo());
        saveCardPaymentInfo(tossResponseDto, payment);
        orderRepository.save(order);
        updateOrderProductState(order);
    }

    /**
     * 주문상품의 상태를 변경해주는 메소드 입니다.
     *
     * @param order 주문.
     */
    private void updateOrderProductState(BookpubOrder order) {
        List<OrderProduct> orderProductList
                = orderProductRepository.getOrderProductList(order.getOrderNo());

        OrderProductStateCode orderProductState =
                orderProductStateRepository
                        .findByCodeName(OrderProductState.WAITING_DELIVERY.getName())
                        .orElseThrow(NotFoundOrderProductStateException::new);

        orderProductList.forEach(op -> {
            op.modifyOrderProductState(orderProductState);
            orderProductRepository.save(op);
        });
    }

    /**
     * 배송서버와 통신하여 배송 db에 저장 하고 송장번호를 받아오는 메소드 입니다.
     *
     * @param order 주문.
     * @return 송장번호.
     */
    private CreateDeliveryResponseDto sendDelivery(BookpubOrder order) {
        return deliveryAdaptor.createDelivery(
                new CreateDeliveryRequestDto(
                        order.getOrderNo(),
                        order.getOrderRequest(),
                        order.getOrderRecipient(),
                        order.getRecipientPhone(),
                        order.getCreatedAt(),
                        order.getAddressDetail()
                ));
    }

    /**
     * 주문 상태를 배송중으로 변경시켜주는 메소드.
     *
     * @param bookpubOrder 변경하고자 하는 주문.
     */
    private void changeOrderStateToWaitDelivery(BookpubOrder bookpubOrder) {
        bookpubOrder.modifyState(orderStateCodeRepository
                .findByCodeName(OrderState.WAITING_DELIVERY.getName())
                .orElseThrow(NotFoundOrderStateException::new));
    }

    /**
     * 카드로 결제 했을 경우 카드결제내역에 데이터 저장하기.
     *
     * @param tossResponseDto 토스결제 후 응답받은 dto.
     * @param payment         결제.
     */
    private void saveCardPaymentInfo(TossResponseDto tossResponseDto, Payment payment) {
        if (Objects.nonNull(tossResponseDto.getCard())) {
            cardRepository.save(new Card(
                    payment.getPaymentNo(),
                    CardCompany.match(tossResponseDto.getCard().getIssuerCode()).getName(),
                    tossResponseDto.getCard().getNumber(),
                    tossResponseDto.getCard().getInstallmentPlanMonths())
            );
        }

    }
}
