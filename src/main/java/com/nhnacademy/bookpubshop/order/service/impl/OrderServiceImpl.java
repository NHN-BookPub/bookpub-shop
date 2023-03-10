package com.nhnacademy.bookpubshop.order.service.impl;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductException;
import com.nhnacademy.bookpubshop.order.relationship.exception.NotFoundOrderProductStateException;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.order.service.OrderService;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.exception.NotFoundOrderStateException;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.exception.SoldOutException;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ?????? ???????????? ?????????.
 *
 * @author : ?????????, ?????????
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private static final String BUY_BOOK = "?????? ??????";
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PricePolicyRepository pricePolicyRepository;
    private final OrderStateCodeRepository orderStateCodeRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderProductStateCodeRepository orderProductStateCodeRepository;
    private final CouponRepository couponRepository;
    private final ProductSaleStateCodeRepository productSaleStateCodeRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetOrderDetailResponseDto getOrderDetailById(Long orderNo) {
        GetOrderDetailResponseDto response = orderRepository.getOrderDetailById(orderNo)
                .orElseThrow(OrderNotFoundException::new);

        response.addProducts(productRepository.getProductListByOrderNo(orderNo));

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetOrderDetailResponseDto getOrderDetailByOrderId(String orderId) {
        GetOrderDetailResponseDto response = orderRepository.getOrderDetailByOrderId(orderId)
                .orElseThrow(OrderNotFoundException::new);

        response.addProducts(productRepository.getProductListByOrderNo(response.getOrderNo()));

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Long createOrder(CreateOrderRequestDto request) {
        Member member = null;
        if (Objects.nonNull(request.getMemberNo())) {
            member = memberRepository.findById(request.getMemberNo())
                    .orElseThrow(MemberNotFoundException::new);
        }

        PricePolicy deliveryPolicy =
                pricePolicyRepository.getPricePolicyById(request.getDeliveryFeePolicyNo())
                        .orElseThrow(NotFoundPricePolicyException::new);

        PricePolicy packagingPricePolicy =
                pricePolicyRepository.getPricePolicyById(request.getPackingFeePolicyNo())
                        .orElseThrow(NotFoundPricePolicyException::new);

        OrderStateCode orderStateCode =
                orderStateCodeRepository.findByCodeName(OrderState.WAITING_PAYMENT.getName())
                        .orElseThrow(NotFoundStateCodeException::new);

        String orderId = UUID.randomUUID().toString().replace("-", "");

        BookpubOrder order = orderRepository.save(BookpubOrder.builder()
                .member(member)
                .deliveryPricePolicy(deliveryPolicy)
                .packagingPricePolicy(packagingPricePolicy)
                .addressDetail(request.getAddressDetail())
                .roadAddress(request.getRoadAddress())
                .orderStateCode(orderStateCode)
                .orderRecipient(request.getRecipientName())
                .recipientPhone(request.getRecipientNumber())
                .orderBuyer(request.getBuyerName())
                .buyerPhone(request.getBuyerNumber())
                .receivedAt(request.getReceivedAt())
                .orderPrice(request.getTotalAmount())
                .pointAmount(request.getPointAmount())
                .pointSave(request.getSavePoint())
                .orderPackaged(request.isPackaged())
                .orderRequest(request.getOrderRequest())
                .couponDiscount(request.getCouponAmount())
                .orderId(orderId)
                .orderName(request.getOrderName())
                .build());

        createOrderProduct(request, order, request.getProductCoupon(),
                request.getProductPointSave());

        if (Objects.nonNull(member)) {
            updateMemberPoint(member.getMemberNo(), request.getPointAmount());
        }

        return order.getOrderNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrderProduct(CreateOrderRequestDto request,
                                   BookpubOrder order,
                                   Map<Long, Long> productCoupon,
                                   Map<Long, Long> productPointSave) {
        OrderProductStateCode orderProductStateCode =
                orderProductStateCodeRepository
                        .findByCodeName(OrderProductState.WAITING_PAYMENT.getName())
                        .orElseThrow(NotFoundStateCodeException::new);

        for (Long productNo : request.getProductNos()) {
            Product product = productRepository.findById(productNo)
                    .orElseThrow(ProductNotFoundException::new);

            updateProductInventory(productNo, request.getProductCount().get(productNo));

            OrderProduct orderProduct = orderProductRepository.save(
                    OrderProduct.builder()
                            .product(product)
                            .order(order)
                            .orderProductStateCode(orderProductStateCode)
                            .productAmount(request.getProductCount().get(productNo))
                            .couponAmount(request.getProductSaleAmount().get(productNo))
                            .productPrice(request.getProductAmount().get(productNo))
                            .reasonName(OrderState.WAITING_PAYMENT.getReason())
                            .pointSave(productPointSave.get(productNo))
                            .build());

            updateCoupon(order, orderProduct, productCoupon.get(productNo));
        }
    }

    /**
     * ????????? ????????? ??????????????? ????????? ?????????.
     *
     * @param order        ??????
     * @param orderProduct ????????????
     * @param couponNo     ????????? ??????
     */
    public void updateCoupon(BookpubOrder order, OrderProduct orderProduct, Long couponNo) {
        if (Objects.isNull(couponNo)) {
            return;
        }
        Coupon coupon = couponRepository.findById(couponNo)
                .orElseThrow(() -> new NotFoundCouponException(couponNo));

        coupon.updateUsed(order, orderProduct);
    }

    /**
     * ????????? ????????? ??????????????? ????????? ?????????.
     *
     * @param memberNo ????????????.
     * @param usePoint ????????? ?????????.
     */
    public void updateMemberPoint(Long memberNo, Long usePoint) {
        if (usePoint == 0) {
            return;
        }

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.decreaseMemberPoint(usePoint);

        pointHistoryRepository.save(
                PointHistory.builder()
                        .pointHistoryReason(BUY_BOOK)
                        .member(member)
                        .pointHistoryAmount(usePoint)
                        .pointHistoryIncreased(false)
                        .build());
    }

    /**
     * ????????? ????????? ??????????????? ????????? ?????????.
     *
     * @param productNo ????????????.
     */
    @Transactional
    public void updateProductInventory(Long productNo, Integer productAmount) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        isSoldOut(productAmount, product);

        makeSoldOut(productAmount, product);

        product.minusStock(productAmount);
    }

    /**
     * ????????? ????????? ????????? ???????????? ???????????? ??????????????????.
     * ????????? ????????? ??? ?????? ????????? ???????????????.
     *
     * @param productAmount ?????? ??? ????????? ???
     * @param product       ?????? ??? ??????
     */
    private void isSoldOut(Integer productAmount, Product product) {
        if (product.getProductSaleStateCode()
                .getCodeCategory().equals(ProductSaleState.SOLD_OUT.getName())
                || product.getProductStock() - productAmount < 0) {
            throw new SoldOutException();
        }
    }

    /**
     * ????????? ????????? ????????? ?????? ???????????? ????????? ??????????????? ???????????????.
     *
     * @param productAmount ?????? ??? ????????? ???
     * @param product       ?????? ??? ??????
     */
    private void makeSoldOut(Integer productAmount, Product product) {
        if (product.getProductStock() - productAmount == 0) {
            product.modifySaleStateCode(
                    productSaleStateCodeRepository
                            .findByCodeCategory(ProductSaleState.STOP.name())
                            .orElseThrow(NotFoundStateCodeException::new));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyInvoiceNumber(Long orderNo, String invoiceNo) {
        BookpubOrder order = orderRepository.findById(orderNo)
                .orElseThrow(OrderNotFoundException::new);

        order.modifyInvoiceNo(invoiceNo);
        orderRepository.save(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyStateCode(
            @StateCode(enumClass = OrderState.class) String stateCode,
            Long orderNo) {
        BookpubOrder order = orderRepository.findById(orderNo)
                .orElseThrow(OrderNotFoundException::new);
        OrderStateCode orderStateCode = orderStateCodeRepository.findByCodeName(stateCode)
                .orElseThrow(NotFoundStateCodeException::new);

        order.modifyState(orderStateCode);
        orderRepository.save(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetOrderListForAdminResponseDto> getOrderList(Pageable pageable) {
        Page<GetOrderListForAdminResponseDto> returns =
                orderRepository.getOrderList(pageable);

        return new PageResponse<>(returns);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetOrderListResponseDto> getOrderListByUsers(
            Pageable pageable, Long memberNo) {
        Page<GetOrderListResponseDto> returns =
                orderRepository.getOrdersListByUser(pageable, memberNo);

        for (GetOrderListResponseDto response : returns.getContent()) {
            response.addOrderProducts(
                    productRepository.getProductListByOrderNo(response.getOrderNo()));
        }

        return new PageResponse<>(returns);
    }

    /**
     * {@inheritDoc}
     *
     * @return ??????, ?????? ??????.
     * @throws OrderNotFoundException ??????????????? ????????????.
     */
    @Override
    public GetOrderAndPaymentResponseDto getOrderAndPaymentInfo(String orderId) {
        return orderRepository.getOrderAndPayment(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     *
     * @throws OrderNotFoundException ??????????????? ????????????.
     */
    @Override
    public GetOrderConfirmResponseDto getOrderConfirmInfo(Long orderNo) {
        return orderRepository.getOrderConfirmInfo(orderNo)
                .orElseThrow(OrderNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void confirmOrderProduct(String orderProductNo, String memberNo) {
        OrderProduct orderProduct = modifyOrderProductState(orderProductNo);
        modifyOrderState(orderProduct);
        increaseMemberPoint(memberNo, orderProduct);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void confirmExchange(String orderProductNo) {
        OrderProduct orderProduct =
                orderProductRepository.getOrderProduct(Long.valueOf(orderProductNo))
                        .orElseThrow(NotFoundOrderProductException::new);

        OrderProductStateCode orderProductStateCode =
                orderProductStateCodeRepository
                        .findByCodeName(OrderProductState.COMPLETE_EXCHANGE.getName())
                        .orElseThrow(NotFoundOrderProductStateException::new);

        orderProduct.modifyOrderProductState(orderProductStateCode);

        Product product =
                productRepository.findById(orderProduct.getProduct().getProductNo())
                        .orElseThrow(ProductNotFoundException::new);

        product.minusStock(orderProduct.getProductAmount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetOrderListForAdminResponseDto> getOrderListByCodeName(Pageable pageable, String codeName) {
        String code = OrderState.CANCEL.getNameByEngName(codeName);

        Page<GetOrderListForAdminResponseDto> responses =
                orderRepository.getOrderListByCodeName(pageable, code);

        return new PageResponse<>(responses);
    }


    /**
     * ?????? ????????? ????????? ?????????????????? ???????????? ?????????.
     *
     * @param orderProductNo ???????????? ??????.
     * @return ????????????.
     */
    private OrderProduct modifyOrderProductState(String orderProductNo) {
        OrderProduct orderProduct =
                orderProductRepository.getOrderProduct(Long.parseLong(orderProductNo))
                        .orElseThrow(NotFoundOrderProductException::new);

        OrderProductStateCode orderProductStateCode =
                orderProductStateCodeRepository
                        .findByCodeName(OrderProductState.CONFIRMED.getName())
                        .orElseThrow(NotFoundOrderProductStateException::new);
        orderProduct.modifyOrderProductState(orderProductStateCode);
        return orderProduct;
    }

    /**
     * ?????? ???????????? ??????????????? ????????? ??????????????? ?????????????????? ?????????.
     *
     * @param memberNo     ????????????.
     * @param orderProduct ????????????.
     */
    private void increaseMemberPoint(String memberNo, OrderProduct orderProduct) {
        Member member = memberRepository.findById(Long.parseLong(memberNo))
                .orElseThrow(MemberNotFoundException::new);
        member.increaseMemberPoint(orderProduct.getPointSave());
        pointHistoryRepository.save(PointHistory.builder()
                .pointHistoryReason(BUY_BOOK)
                .member(member)
                .pointHistoryIncreased(true)
                .pointHistoryAmount(orderProduct.getPointSave())
                .build()
        );
    }

    /**
     * ??????????????? ?????????????????? ?????????????????? ?????????.
     *
     * @param orderProduct ????????????.
     */
    private void modifyOrderState(OrderProduct orderProduct) {
        BookpubOrder bookpubOrder = orderRepository.findById(orderProduct.getOrder().getOrderNo())
                .orElseThrow(OrderNotFoundException::new);

        if (bookpubOrder.getOrderStateCode().getCodeName().equals(OrderState.CONFIRMED.getName())) {
            return;
        }

        OrderStateCode orderStateCode =
                orderStateCodeRepository.findByCodeName(OrderState.CONFIRMED.getName())
                        .orElseThrow(NotFoundOrderStateException::new);
        bookpubOrder.modifyState(orderStateCode);
    }
}
