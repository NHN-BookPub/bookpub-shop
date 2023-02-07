package com.nhnacademy.bookpubshop.order.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 주문상품 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class OrderProductRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderProductRepository orderProductRepository;

    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    BookpubOrder order;
    OrderProductStateCode orderProductStateCode;
    OrderProduct orderProduct;
    Member member;
    BookPubTier bookPubTier;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        orderProductStateCode = new OrderProductStateCode(null, OrderProductState.CONFIRMED.getName(), OrderProductState.CONFIRMED.isUsed(), "주문완료되었습니다.");

        bookPubTier = TierDummy.dummy();

        member = MemberDummy.dummy(bookPubTier);

        order = new BookpubOrder(
                null,
                member,
                new PricePolicy(null, "배송비", 3000L),
                new PricePolicy(null, "포장비", 1500L),
                new OrderStateCode(null, OrderState.COMPLETE_DELIVERY.getName(), OrderState.COMPLETE_DELIVERY.isUsed(), "배송완료"),
                "test_recipient",
                "test_recipient_phone",
                "test_buyer",
                "test_buyer_phone",
                LocalDateTime.now(),
                null,
                10000L,
                100L,
                true,
                null,
                1000L,
                "IT관",
                "광주 동구 조선대길",
                "dsafijvxzkjs",
                "orderName"
        );
        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(productPolicy);
        entityManager.persist(order.getPackagingPricePolicy());
        entityManager.persist(order.getDeliveryPricePolicy());
        entityManager.persist(order.getOrderStateCode());
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(orderProductStateCode);
        entityManager.persist(product);
        entityManager.persist(order);


        orderProduct = new OrderProduct(null, product, order, orderProductStateCode, 1, 1000L, product.getSalesPrice(), null);
    }

    @Test
    @DisplayName("주문상품 save 테스트")
    void memberSaveTest() {
        OrderProduct persist = entityManager.persist(orderProduct);
        Optional<OrderProduct> result = orderProductRepository.findById(persist.getOrderProductNo());

        assertThat(result).isPresent();
        assertThat(result.get().getOrderProductNo()).isEqualTo(persist.getOrderProductNo());
        assertThat(result.get().getProduct().getTitle()).isEqualTo(persist.getProduct().getTitle());
        assertThat(result.get().getOrder().getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(result.get().getOrderProductStateCode().getCodeNo()).isEqualTo(orderProductStateCode.getCodeNo());
        assertThat(result.get().getProductAmount()).isEqualTo(persist.getProductAmount());
        assertThat(result.get().getCouponAmount()).isEqualTo(persist.getCouponAmount());
        assertThat(result.get().getProductPrice()).isEqualTo(persist.getProductPrice());
        assertThat(result.get().getReasonName()).isEqualTo(persist.getReasonName());
    }
}