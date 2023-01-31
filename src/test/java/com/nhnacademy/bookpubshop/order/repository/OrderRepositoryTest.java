package com.nhnacademy.bookpubshop.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *  주문 Repo Test 입니다.
 *
 * @author : 김서현, 여운석
 * @since : 1.0
 **/
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    BookpubOrder order;
    BookPubTier bookPubTier;
    Member member;
    PricePolicy pricePolicy;
    PricePolicy packagePricePolicy;
    Product product;
    OrderStateCode orderStateCode;
    ProductPolicy productPolicy;
    ProductSaleStateCode productSaleStateCode;
    ProductTypeStateCode productTypeStateCode;
    OrderProduct orderProduct;
    OrderProductStateCode orderProductStateCode;


    @BeforeEach
    void setUp() {

        bookPubTier = TierDummy.dummy();
        bookPubTier = entityManager.persist(bookPubTier);

        member = MemberDummy.dummy(bookPubTier);
        member = entityManager.persist(member);

        pricePolicy = PricePolicyDummy.dummy();
        pricePolicy = entityManager.persist(pricePolicy);

        packagePricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = entityManager.persist(packagePricePolicy);

        orderStateCode = OrderStateCodeDummy.dummy();
        orderStateCode = entityManager.persist(orderStateCode);

        productPolicy = ProductPolicyDummy.dummy();
        productPolicy = entityManager.persist(productPolicy);

        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productTypeStateCode = entityManager.persist(productTypeStateCode);

        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        productSaleStateCode = entityManager.persist(productSaleStateCode);

        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, orderStateCode);
        order = entityManager.persist(order);

        orderProductStateCode = new OrderProductStateCode(
                null,
                OrderProductState.COMPLETE.getName(),
                true,
                "info");
        orderProductStateCode = entityManager.persist(orderProductStateCode);

        product = ProductDummy.dummy(productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                Collections.EMPTY_LIST);

        product = entityManager.persist(product);

        orderProduct = new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");
        orderProduct = entityManager.persist(orderProduct);

    }

    @Test
    @DisplayName("주문 save 테스트")
    void orderSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Optional<BookpubOrder> result = orderRepository.findById(order.getOrderNo());

        assertThat(result).isPresent();
        assertThat(result.get().getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(order.getMember().getMemberId());
        assertThat(result.get().getDeliveryPricePolicy().getPolicyNo()).isEqualTo(order.getDeliveryPricePolicy().getPolicyNo());
        assertThat(result.get().getDeliveryPricePolicy().getPolicyName()).isEqualTo(order.getDeliveryPricePolicy().getPolicyName());
        assertThat(result.get().getAddressDetail()).isEqualTo(order.getAddressDetail());
        assertThat(result.get().getRoadAddress()).isEqualTo(order.getRoadAddress());
        assertThat(result.get().getOrderStateCode().getCodeNo()).isEqualTo(order.getOrderStateCode().getCodeNo());
        assertThat(result.get().getOrderRecipient()).isEqualTo(order.getOrderRecipient());
        assertThat(result.get().getRecipientPhone()).isEqualTo(order.getRecipientPhone());
        assertThat(result.get().getOrderBuyer()).isEqualTo(order.getOrderBuyer());
        assertThat(result.get().getBuyerPhone()).isEqualTo(order.getBuyerPhone());
        assertThat(result.get().getReceivedAt()).isEqualTo(order.getReceivedAt());
        assertThat(result.get().getInvoiceNumber()).isEqualTo(order.getInvoiceNumber());
        assertThat(result.get().getOrderPrice()).isEqualTo(order.getOrderPrice());
        assertThat(result.get().getPointAmount()).isEqualTo(order.getPointAmount());
        assertThat(result.get().isOrderPackaged()).isEqualTo(order.isOrderPackaged());
        assertThat(result.get().getOrderRequest()).isEqualTo(order.getOrderRequest());
        assertThat(result.get().getCouponDiscount()).isEqualTo(order.getCouponDiscount());
        assertThat(result.get().getCreatedAt()).isBefore(now);

        entityManager.clear();
    }

    @Test
    @DisplayName("주문 상세 단건 조회 성공")
    void getOrderDetailById() {
        BookpubOrder persist = order;

        GetOrderDetailResponseDto result =
                orderRepository.getOrderDetailById(persist.getOrderNo())
                        .orElseThrow(OrderNotFoundException::new);

        assertThat(result.getOrderNo()).isEqualTo(persist.getOrderNo());
        assertThat(result.getOrderState()).isEqualTo(persist.getOrderStateCode().getCodeName());
        assertThat(result.getAddressDetail()).isEqualTo(persist.getAddressDetail());
        assertThat(result.getAddressBase()).isEqualTo(persist.getRoadAddress());
        assertThat(result.getOrderRequest()).isEqualTo(persist.getOrderRequest());
        assertThat(result.getBuyerName()).isEqualTo(persist.getOrderBuyer());
        assertThat(result.getBuyerNumber()).isEqualTo(persist.getBuyerPhone());
        assertThat(result.getRecipientName()).isEqualTo(persist.getOrderRecipient());
        assertThat(result.getRecipientNumber()).isEqualTo(persist.getRecipientPhone());
        assertThat(result.getInvoiceNo()).isEqualTo(persist.getInvoiceNumber());
        assertThat(result.getDeliveryAmount()).isEqualTo(persist.getDeliveryPricePolicy().getPolicyFee());
        assertThat(result.getPackageAmount()).isEqualTo(persist.getPackagingPricePolicy().getPolicyFee());
        assertThat(result.getCouponAmount()).isEqualTo(persist.getCouponDiscount());
        assertThat(result.getTotalAmount()).isEqualTo(persist.getOrderPrice());
        assertThat(result.getReceivedAt()).isEqualTo(persist.getReceivedAt());
        assertThat(result.isPackaged()).isEqualTo(persist.isOrderPackaged());

        List<GetProductListForOrderResponseDto> productsResult =
                productRepository.getProductListByOrderNo(persist.getOrderNo());

        assertThat(productsResult.get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(productsResult.get(0).getTitle()).isEqualTo(product.getTitle());
        assertThat(productsResult.get(0).getSalesPrice()).isEqualTo(product.getSalesPrice());
        assertThat(productsResult.get(0).getProductAmount()).isEqualTo(orderProduct.getProductAmount());
    }

    @Test
    @DisplayName("관리자를 위한 주문 전체 조회")
    void getOrdersList() {
        BookpubOrder persist = order;

        Pageable pageable = PageRequest.of(0,10);

        Page<GetOrderListForAdminResponseDto> result = orderRepository.getOrdersList(pageable);

        assertThat(result.getContent().get(0).getOrderNo())
                .isEqualTo(persist.getOrderNo());
        assertThat(result.getContent().get(0).getOrderState())
                .isEqualTo(persist.getOrderStateCode().getCodeName());
        assertThat(result.getContent().get(0).getTotalAmount())
                .isEqualTo(persist.getOrderPrice());
        assertThat(result.getContent().get(0).getInvoiceNo())
                .isEqualTo(persist.getInvoiceNumber());
        assertThat(result.getContent().get(0).getCreatedAt())
                .isEqualTo(persist.getCreatedAt());
        assertThat(result.getContent().get(0).getReceivedAt())
                .isEqualTo(persist.getReceivedAt());
    }

    @Test
    @DisplayName("유저 번호로 주문 리스트 조회 성공")
    void getOrdersListByUser() {
        BookpubOrder persist = order;

        Pageable pageable = PageRequest.of(0,10);

        Page<GetOrderListResponseDto> result = orderRepository.getOrdersListByUser(pageable, member.getMemberNo());

        assertThat(result.getContent().get(0).getOrderNo())
                .isEqualTo(persist.getOrderNo());
        assertThat(result.getContent().get(0).getOrderState())
                .isEqualTo(persist.getOrderStateCode().getCodeName());
        assertThat(result.getContent().get(0).getTotalAmount())
                .isEqualTo(persist.getOrderPrice());
        assertThat(result.getContent().get(0).getInvoiceNo())
                .isEqualTo(persist.getInvoiceNumber());
        assertThat(result.getContent().get(0).getCreatedAt())
                .isEqualTo(persist.getCreatedAt());
        assertThat(result.getContent().get(0).getReceivedAt())
                .isEqualTo(persist.getReceivedAt());

        List<GetProductListForOrderResponseDto> response = productRepository.getProductListByOrderNo(persist.getOrderNo());

        assertThat(response.get(0).getProductNo())
                .isEqualTo(product.getProductNo());
        assertThat(response.get(0).getTitle())
                .isEqualTo(product.getTitle());
        assertThat(response.get(0).getProductAmount())
                .isEqualTo(orderProduct.getProductAmount());
        assertThat(response.get(0).getSalesPrice())
                .isEqualTo(product.getSalesPrice());
    }
}