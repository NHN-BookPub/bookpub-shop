package com.nhnacademy.bookpubshop.payment.service.impl;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductException;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductStateException;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.exception.NotFoundOrderStateException;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.request.OrderProductRefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.response.GetRefundResponseDto;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.payment.event.PaymentEvent;
import com.nhnacademy.bookpubshop.payment.exception.NotFoundPaymentException;
import com.nhnacademy.bookpubshop.payment.repository.PaymentRepository;
import com.nhnacademy.bookpubshop.payment.service.PaymentService;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymentstatecode.exception.NotFoundPaymentStateException;
import com.nhnacademy.bookpubshop.paymentstatecode.repository.PaymentStateCodeRepository;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.PaymentState;
import com.nhnacademy.bookpubshop.state.PaymentTypeState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ?????? ????????? ????????? ?????????.
 *
 * @author : ?????????
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@EnableAsync
@Slf4j
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentStateCodeRepository stateRepository;
    private final PaymentTypeRepository typeRepository;
    private final OrderStateCodeRepository orderStateRepository;
    private final OrderProductStateCodeRepository orderProductStateRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final TossAdaptor tossAdaptor;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean verifyPayment(String orderId, Long amount) {
        GetOrderVerifyResponseDto getOrderVerifyResponseDto
                = orderRepository.verifyPayment(orderId)
                .orElseThrow(OrderNotFoundException::new);

        return Objects.equals(getOrderVerifyResponseDto.getAmount(), amount);
    }

    /**
     * {@inheritDoc}
     */
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
     * ?????? ??? ????????? ???????????? ?????????.
     *
     * @param orderId ?????? id
     * @return ??????
     */
    private BookpubOrder getBookpubOrder(String orderId) {
        return orderRepository.getOrderByOrderKey(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    /**
     * ??????????????? ???????????? ?????????.
     *
     * @return ????????????.
     */
    private PaymentStateCode getPaymentStateCode() {
        return stateRepository.getPaymentStateCode(PaymentState.COMPLETE_PAYMENT.getName())
                .orElseThrow(NotFoundPaymentStateException::new);
    }

    /**
     * ?????? ?????? ??????????????? ???????????? ?????????.
     *
     * @return ????????????????????????.
     */
    private PaymentTypeStateCode getPaymentTypeStateCode() {
        return typeRepository.getPaymentType(PaymentTypeState.CARD.getName())
                .orElseThrow(NotFoundPaymentStateException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void refundOrder(RefundRequestDto refundRequestDto) {
        TossResponseDto tossResponseDto = getTossResponseDto(refundRequestDto);

        paymentUpdate(refundRequestDto, tossResponseDto);
        BookpubOrder bookpubOrder = updateOrderState(tossResponseDto);
        List<OrderProduct> orderProducts = updateOrderProductState(bookpubOrder);
        increaseProductStock(orderProducts);
        updateCoupon(bookpubOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void refundOrderProduct(OrderProductRefundRequestDto requestDto) {
        boolean isAllUpdate = allUpdateState(requestDto);

        if (!isAllUpdate) {
            OrderProduct orderProduct =
                    orderProductRepository.getOrderProduct(requestDto.getOrderProductNo())
                            .orElseThrow(NotFoundOrderProductException::new);

            updateOrderProductCoupon(orderProduct);
            increaseProductStock(List.of(orderProduct));

            OrderProductStateCode orderProductStateCode =
                    orderProductStateRepository.findByCodeName(OrderProductState.REFUND.getName())
                            .orElseThrow(NotFoundOrderProductStateException::new);

            orderProduct.modifyOrderProductState(orderProductStateCode);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void exchangeOrderProduct(OrderProductRefundRequestDto exchangeRequestDto) {
        OrderProduct orderProduct =
                orderProductRepository.getOrderProduct(exchangeRequestDto.getOrderProductNo())
                .orElseThrow(NotFoundOrderProductException::new);

        OrderProductStateCode orderProductStateCode =
                orderProductStateRepository
                        .findByCodeName(OrderProductState.WAITING_EXCHANGE.getName())
                .orElseThrow(NotFoundOrderProductStateException::new);

        orderProduct.updateExchangeReason(exchangeRequestDto.getCancelReason());
        orderProduct.modifyOrderProductState(orderProductStateCode);
    }

    /**
     * ?????? ????????? ?????? ????????????????????? ?????? ??????, ????????? ????????? ???????????? ?????? ????????? ???????????? ????????? ?????????.
     *
     * @param requestDto ??????dto.
     * @return ?????? ????????? ?????????????????? ?????????.
     */
    private boolean allUpdateState(OrderProductRefundRequestDto requestDto) {
        List<OrderProduct> orderProductList
                = orderProductRepository.getOrderProductList(requestDto.getOrderNo());

        int refundCnt = getRefundCnt(orderProductList);

        boolean isPaymentStateUpdate = finalRefundProduct(refundCnt, orderProductList);
        judgeUpdateAll(requestDto, isPaymentStateUpdate);

        return isPaymentStateUpdate;
    }

    /**
     * ?????? ??? ?????? ??? ??????????????? ????????? ???????????? ?????????.
     *
     * @param orderProductList ???????????? ?????????.
     * @return ????????? ???????????? ??????
     */
    private static int getRefundCnt(List<OrderProduct> orderProductList) {
        int refundCnt = 0;

        for (OrderProduct op : orderProductList) {
            if (op.getOrderProductStateCode().getCodeName()
                    .equals(OrderProductState.REFUND.getName())) {
                refundCnt++;
            }
        }
        return refundCnt;
    }

    /**
     * ?????? ????????? ???????????? ???????????? ???????????? ?????? ?????? ??????????????? ???????????????.
     *
     * @param requestDto           ????????????.
     * @param isPaymentStateUpdate ?????????????????? ?????? ??????.
     */
    private void judgeUpdateAll(OrderProductRefundRequestDto requestDto,
                                boolean isPaymentStateUpdate) {
        if (isPaymentStateUpdate) {
            refundOrder(new RefundRequestDto(requestDto.getOrderNo(),
                    requestDto.getCancelReason()));
        }
    }

    /**
     * ???????????? ??? ????????? ?????? ?????????????????? ???????????? ????????? ?????? ????????????????????? ??????, ???????????? ?????? ???????????? ?????????.
     *
     * @param refundCnt        ????????? ?????? ??????.
     * @param orderProductList ???????????? ?????????.
     * @return true, false
     */
    private static boolean finalRefundProduct(int refundCnt, List<OrderProduct> orderProductList) {
        return refundCnt + 1 == orderProductList.size();
    }

    /**
     * ????????? ???????????? ????????? ????????? ??????????????? ??????????????? ????????? ?????????.
     *
     * @param order ??????.
     */
    private void updateCoupon(BookpubOrder order) {
        List<Coupon> couponList
                = couponRepository.findByCouponByOrderNo(order.getOrderNo());
        couponList.forEach(Coupon::modifyNotUsedCoupon);
    }

    /**
     * ??????????????? ???????????? ????????? ????????? ??????????????? ??????????????? ????????? ?????????.
     *
     * @param orderProduct ????????????.
     */
    private void updateOrderProductCoupon(OrderProduct orderProduct) {
        List<Coupon> couponList
                = couponRepository.findByCouponByOrderProductNo(orderProduct.getOrderProductNo());
        couponList.forEach(Coupon::modifyNotUsedCoupon);
    }

    /**
     * ???????????? ???????????? ????????? ?????? ???????????? ?????????.
     *
     * @param orderProducts ???????????????.
     */
    private void increaseProductStock(List<OrderProduct> orderProducts) {
        orderProducts.forEach(
                op -> {
                    Product product = productRepository.findById(op.getProduct().getProductNo())
                            .orElseThrow(ProductNotFoundException::new);
                    product.plusStock(op.getProductAmount());
                }
        );
    }

    /**
     * ?????? ???????????? ?????????????????? ???????????? ?????????.
     *
     * @param order ????????????.
     */
    private List<OrderProduct> updateOrderProductState(BookpubOrder order) {
        OrderProductStateCode orderProductStateCode
                = orderProductStateRepository
                .findByCodeName(OrderProductState.REFUND.getName())
                .orElseThrow(NotFoundOrderProductStateException::new);

        List<OrderProduct> orderProductList =
                orderRepository.getOrderProductList(order.getOrderNo());

        orderProductList.forEach(
                orderProduct -> orderProduct.modifyOrderProductState(orderProductStateCode));

        return orderProductList;
    }

    /**
     * ????????? ????????? ???????????? ?????????.
     * ??????????????? ??????.
     *
     * @param tossResponseDto ??????????????????.
     * @return ??????.
     */
    private BookpubOrder updateOrderState(TossResponseDto tossResponseDto) {
        BookpubOrder bookpubOrder =
                paymentRepository.getOrderByPaymentKey(tossResponseDto.getPaymentKey())
                        .orElseThrow(OrderNotFoundException::new);

        OrderStateCode orderStateCode =
                orderStateRepository.findByCodeName(OrderState.CANCEL_PAYMENT.getName())
                        .orElseThrow(NotFoundOrderStateException::new);

        bookpubOrder.modifyState(orderStateCode);

        return bookpubOrder;
    }

    /**
     * ????????? ?????? ????????? ?????? ????????????????????? ???????????? ?????????.
     *
     * @param refundRequestDto ???????????? dto.
     * @return ??????????????????.
     */
    private TossResponseDto getTossResponseDto(RefundRequestDto refundRequestDto) {
        GetRefundResponseDto getRefundResponseDto =
                paymentRepository.getRefundInfo(refundRequestDto)
                        .orElseThrow(NotFoundPaymentException::new);

        return tossAdaptor.requestRefund(
                getRefundResponseDto.getPaymentKey(),
                refundRequestDto.getCancelReason());
    }

    /**
     * ?????? ??????????????? ?????? ?????? ????????? ??????????????? ?????????.
     *
     * @param refundRequestDto ???????????? dto.
     * @param tossResponseDto  ???????????? dto.
     */
    private void paymentUpdate(RefundRequestDto refundRequestDto, TossResponseDto tossResponseDto) {
        Payment payment = paymentRepository.getPayment(tossResponseDto.getPaymentKey())
                .orElseThrow(NotFoundPaymentException::new);

        PaymentStateCode paymentStateCode =
                stateRepository.getPaymentStateCode(PaymentState.CANCEL_PAYMENT.getName())
                        .orElseThrow(NotFoundPaymentStateException::new);

        payment.refund(paymentStateCode, refundRequestDto.getCancelReason());
    }

}
