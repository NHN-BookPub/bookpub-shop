package com.nhnacademy.bookpubshop.order.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetExchangeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.dummy.OrderProductDummy;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 주문상품 레포지토리 테스트
 *
 * @author : 여운석, 임태원
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
    PricePolicy shipPricePolicy;
    PricePolicy packPricePolicy;
    OrderStateCode orderStateCode;

    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        orderProductStateCode = new OrderProductStateCode(null, OrderProductState.WAITING_EXCHANGE.getName(), OrderProductState.WAITING_EXCHANGE.isUsed(), "주문완료되었습니다.");
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        shipPricePolicy = PricePolicyDummy.dummy();
        packPricePolicy = PricePolicyDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();

        order = OrderDummy.dummy3(member,packPricePolicy,shipPricePolicy,orderStateCode);
        orderProduct = OrderProductDummy.dummy(product,order,orderProductStateCode);

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
        orderProduct = entityManager.persist(orderProduct);
    }

    @Test
    @DisplayName("주문상품 save 테스트")
    void memberSaveTest() {
        OrderProduct persist = orderProduct;
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

    @Test
    @DisplayName("주문상품 리스트를 불러오는 테스트")
    void getOrderProductList() {
        List<OrderProduct> orderProductList
                = orderProductRepository.getOrderProductList(order.getOrderNo());

        assertThat(orderProductList).hasSize(1);
        assertThat(orderProductList.get(0).getExchangeReason()).isEqualTo("변심");
    }

    @Test
    @DisplayName("주문상품 단건 조회 메소드")
    void getOrderProduct() {
        Optional<OrderProduct> orderProduct1
                = orderProductRepository.getOrderProduct(orderProduct.getOrderProductNo());

        assertThat(orderProduct1).isPresent();
    }

    @Test
    @DisplayName("교환상품 리스트를 불러오는 메소드")
    void getExchangeOrderProductList() {
        Pageable pageable = Pageable.ofSize(10);
        Page<GetExchangeResponseDto> exchangeOrderProductList
                = orderProductRepository.getExchangeOrderProductList(pageable);

        assertThat(exchangeOrderProductList.getTotalPages()).isEqualTo(1);
        assertThat(exchangeOrderProductList.getContent()).hasSize(1);
        assertThat(exchangeOrderProductList.getContent().get(0).getOrderProductNo()).isEqualTo(orderProduct.getOrderProductNo());
        assertThat(exchangeOrderProductList.getContent().get(0).getMemberId()).isEqualTo("test_id");
        assertThat(exchangeOrderProductList.getContent().get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(exchangeOrderProductList.getContent().get(0).getThumbnail()).isNull();
        assertThat(exchangeOrderProductList.getContent().get(0).getProductAmount()).isEqualTo(orderProduct.getProductAmount());
        assertThat(exchangeOrderProductList.getContent().get(0).getStateCode()).isEqualTo("교환대기");
        assertThat(exchangeOrderProductList.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(exchangeOrderProductList.getContent().get(0).getExchangeReason()).isEqualTo("변심");
    }
}