package com.nhnacademy.bookpubshop.order.service.impl;

import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.order.service.OrderService;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
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
 * 멤버 서비스의 구현체.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PricePolicyRepository pricePolicyRepository;
    private final OrderStateCodeRepository orderStateCodeRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderProductStateCodeRepository orderProductStateCodeRepository;
    private final CouponRepository couponRepository;
    private final ProductSaleStateCodeRepository productSaleStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
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
                .orderPackaged(request.isPackaged())
                .orderRequest(request.getOrderRequest())
                .couponDiscount(request.getCouponAmount())
                .orderId(orderId)
                .orderName(request.getOrderName())
                .build());

        createOrderProduct(request, order, request.getProductCoupon());

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
                                   Map<Long, Long> productCoupon) {
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
                            .build());

            updateCoupon(order, orderProduct, productCoupon.get(productNo));
        }
    }

    /**
     * 쿠폰의 상태를 변경시키는 메소드 입니다.
     *
     * @param order        주문
     * @param orderProduct 주문상품
     * @param couponNo     사용한 쿠폰
     */
    public void updateCoupon(BookpubOrder order, OrderProduct orderProduct, Long couponNo) {
        if (Objects.isNull(couponNo)) {
            return;
        }
        Coupon coupon = couponRepository.findById(couponNo)
                .orElseThrow(() -> new NotFoundCouponException(couponNo));
        coupon.modifyOrder(order);
        coupon.modifyOrderProduct(orderProduct);
        coupon.couponUsed();
    }

    /**
     * 회원의 상태를 변경시키는 메소드 입니다.
     *
     * @param memberNo 회원번호.
     * @param usePoint 사용한 포인트.
     */
    public void updateMemberPoint(Long memberNo, Long usePoint) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.decreaseMemberPoint(usePoint);
    }

    /**
     * 상품의 상태를 변경시키는 메소드 입니다.
     *
     * @param productNo 상품번호.
     */
    public void updateProductInventory(Long productNo, Integer productAmount) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        isSoldOut(productAmount, product);

        makeSoldOut(productAmount, product);

        product.minusStock(productAmount);
    }

    /**
     * 주문시 상품의 재고가 부족한지 체크하는 메소드입니다.
     * 재고가 부족할 시 품절 예외가 발생합니다.
     *
     * @param productAmount 주문 할 상품의 양
     * @param product 주문 할 상품
     */
    private void isSoldOut(Integer productAmount, Product product) {
        if (product.getProductSaleStateCode()
                .getCodeCategory().equals(ProductSaleState.SOLD_OUT.getName())
                || product.getProductStock() - productAmount < 0) {
            throw new SoldOutException();
        }
    }

    /**
     * 상품의 재고가 주문한 양과 동일하면 상품을 품절상태로 변경합니다.
     *
     * @param productAmount 주문 할 상품의 양
     * @param product 주문 할 상품
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
    @Transactional(readOnly = true)
    public PageResponse<GetOrderListForAdminResponseDto> getOrderList(Pageable pageable) {
        Page<GetOrderListForAdminResponseDto> returns =
                orderRepository.getOrdersList(pageable);

        return new PageResponse<>(returns);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
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
}
