package com.nhnacademy.bookpubshop.order.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderSubscribe;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
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
 * 구독주문상품 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class OrderSubscribeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderSubscribeRepository orderSubscribeRepository;

    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    OrderProduct orderProduct;
    BookpubOrder order;
    OrderProductStateCode orderProductStateCode;
    OrderSubscribe orderSubscribe;
    PricePolicy pricePolicy;
    PricePolicy deliveryPricePolicy;
    Member member;
    Tier tier;
    Address address;
    Subscribe subscribe;
    OrderStateCode orderStateCode;
    OrderSubscribeStateCode orderSubscribeStateCode;



    @BeforeEach
    void setUp() {

        productPolicy = new ProductPolicy(null, "실구매가", true, 5);
        productTypeStateCode = new ProductTypeStateCode(null, "기본", true, "기본입니다.");
        productSaleStateCode = new ProductSaleStateCode(null, "판타지", true, "판타지 소설");
        pricePolicy = PricePolicyDummy.dummy();
        tier = TierDummy.dummy();
        address = AddressDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        deliveryPricePolicy = PricePolicyDummy.dummy();
        orderSubscribeStateCode = new OrderSubscribeStateCode(null, "test", true, "test_info");
        orderProductStateCode = new OrderProductStateCode(null, "test", true, "test_info");
        member = MemberDummy.dummy(tier);
        order = OrderDummy.dummy(member, pricePolicy, deliveryPricePolicy, address, orderStateCode);
        subscribe = SubscribeDummy.dummy();
        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode, "1231231231", "인어공주",
                100, "인어공주 이야기", "mermaid.png", "mermaid_ebook.pdf", 1000L,
                10, 300L, 3, false, 30, LocalDateTime.now(), LocalDateTime.now(), false);
        orderProduct = new OrderProduct(null, product, order, orderProductStateCode, 1, 100L, 5000L, "테스트");
        orderSubscribe = new OrderSubscribe(null, subscribe, order, orderSubscribeStateCode, 1000L, 10000L, null, LocalDateTime.now());

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(pricePolicy);
        entityManager.persist(tier);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(deliveryPricePolicy);
        entityManager.persist(orderSubscribeStateCode);
        entityManager.persist(orderProductStateCode);
        entityManager.persist(member);
        entityManager.persist(product);
        entityManager.persist(order);
        entityManager.persist(subscribe);
        entityManager.persist(orderProduct);
    }

    @Test
    @DisplayName("주문구독 save 테스트")
    void orderSubscribeSaveTest() {
        OrderSubscribe persist = entityManager.persist(orderSubscribe);

        Optional<OrderSubscribe> orderSubscribe = orderSubscribeRepository.findById(persist.getSubscribeNo());

        assertThat(orderSubscribe).isPresent();
        assertThat(orderSubscribe.get().getSubscribeNo()).isEqualTo(persist.getSubscribeNo());
        assertThat(orderSubscribe.get().getOrder()).isEqualTo(persist.getOrder());
        assertThat(orderSubscribe.get().getSubscribe()).isEqualTo(persist.getSubscribe());
        assertThat(orderSubscribe.get().getOrderSubscribeStateCode()).isEqualTo(persist.getOrderSubscribeStateCode());
        assertThat(orderSubscribe.get().getCreatedAt()).isEqualTo(persist.getCreatedAt());
        assertThat(orderSubscribe.get().getCouponAmount()).isEqualTo(persist.getCouponAmount());
        assertThat(orderSubscribe.get().getFinishedAt()).isEqualTo(persist.getFinishedAt());
        assertThat(orderSubscribe.get().getProductPrice()).isEqualTo(persist.getProductPrice());
    }
}