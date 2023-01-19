package com.nhnacademy.bookpubshop.order.service.impl;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dto.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
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
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.PricePolicyState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import com.nhnacademy.bookpubshop.tier.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import lombok.RequiredArgsConstructor;
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
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PricePolicyRepository pricePolicyRepository;
    private final OrderStateCodeRepository orderStateCodeRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderProductStateCodeRepository orderProductStateCodeRepository;

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
    @Transactional
    public void createOrder(CreateOrderRequestDto request, Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        PricePolicy deliveryPolicy = pricePolicyRepository
                .getByPolicyName(PricePolicyState.SHIPPING.getName())
                .orElseThrow(NotFoundPricePolicyException::new);

        PricePolicy packagingPricePolicy = pricePolicyRepository
                .getByPolicyName(PricePolicyState.PACKAGING.getName())
                .orElseThrow(NotFoundPricePolicyException::new);

        OrderStateCode orderStateCode =
                orderStateCodeRepository.findByCodeName(OrderState.WAITING_PAYMENT.getName())
                        .orElseThrow(NotFoundStateCodeException::new);

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
                .build());

        createOrderProduct(request, order);
    }

    /**
     * {@inheritDoc}
     */
    private void createOrderProduct(CreateOrderRequestDto request, BookpubOrder order) {
        OrderProductStateCode orderProductStateCode =
                orderProductStateCodeRepository
                        .findByCodeName(OrderProductState.COMPLETE.getName())
                        .orElseThrow(NotFoundStateCodeException::new);

        for (Long productNo : request.getProductNos()) {
            Product product = productRepository.findById(productNo)
                    .orElseThrow(ProductNotFoundException::new);

            orderProductRepository.save(
                    OrderProduct.builder()
                    .product(product)
                    .order(order)
                    .orderProductStateCode(orderProductStateCode)
                    .productAmount(request.getProductAmounts().get(productNo))
                    .couponAmount(request.getProductCouponAmounts().get(productNo))
                    .productPrice(product.getSalesPrice() * request.getProductAmounts()
                            .get(productNo))
                    .reasonName(request.getOrderProductReasons().get(productNo))
                    .build());
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
    public PageResponse<GetOrderListResponseDto> getOrderList(Pageable pageable) {
        Page<GetOrderListResponseDto> returns =
                orderRepository.getOrdersList(pageable);

        for(GetOrderListResponseDto response : returns.getContent()) {
            response.addOrderProducts(productRepository.getProductListByOrderNo(response.getOrderNo()));
        }

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

        for(GetOrderListResponseDto response : returns.getContent()) {
            response.addOrderProducts(productRepository.getProductListByOrderNo(response.getOrderNo()));
        }

        return new PageResponse<>(returns);
    }
}
