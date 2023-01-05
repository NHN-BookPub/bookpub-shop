package com.nhnacademy.bookpubshop.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.entity.Order;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
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
    Order order;
    OrderProductStateCode orderProductStateCode;
    OrderProduct orderProduct;
    Member member;
    Tier tier;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(null, "실구매가", true, 5);
        productTypeStateCode = new ProductTypeStateCode(null, "기본", true, "기본입니다.");
        productSaleStateCode = new ProductSaleStateCode(null, "판타지", true, "판타지 소설");
        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode, "1231231231", "인어공주",
                100, "인어공주 이야기", "mermaid.png", "mermaid_ebook.pdf", 10000L,
                10, 300L, 3, false, 30, LocalDateTime.now(), LocalDateTime.now(), false);

        orderProductStateCode = new OrderProductStateCode(null, OrderProductState.CONFIRMED.getName(), OrderProductState.CONFIRMED.isUsed(), "주문완료되었습니다.");

        tier = new Tier(null, "브론즈");

        member = new Member(null, tier, "member", "멤버", "사람", "남",
                2000, 12, "pwdpwdpwd", "01000000000", "asd@asd.com",
                LocalDateTime.now(), false, false, null, 0L, false);

        order = new Order(
                null,
                member,
                new PricePolicy(1, "배송비", 3000L),
                new PricePolicy(2, "포장비", 1500L),
                new Address(null,"51550", "광주광역시 동구 어딘가"),
                new OrderStateCode(null, OrderState.COMPLETE_DELIVERY.getName(), OrderState.COMPLETE_DELIVERY.isUsed(), "배송완료"),
                LocalDateTime.now(),
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
                1000L
        );

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(orderProductStateCode);
        entityManager.persist(product);
        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(order);


        orderProduct = new OrderProduct(null, product, order, orderProductStateCode, 1, 1000L, product.getSalesPrice(), null);
    }

    @Test
    @DisplayName("주문상품 save 테스트")
    void memberSaveTest() {
        entityManager.persist(orderProduct);

        Optional<OrderProduct> result = orderProductRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getOrderProductNo()).isEqualTo(1L);
        assertThat(result.get().getProduct().getTitle()).isEqualTo("인어공주");

    }
}