package com.nhnacademy.bookpubshop.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 *  주문 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    BookpubOrder order;

    Tier tier;
    Member member;

    PricePolicy pricePolicy;
    PricePolicy packagePricePolicy;
    Address address;

    OrderStateCode orderStateCode;

    @BeforeEach
    void setUp() {

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        pricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        address = AddressDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        order = OrderDummy.dummy(member,pricePolicy,packagePricePolicy,address,orderStateCode);
    }

    @Test
    @DisplayName("주문 save 테스트")
    void orderSaveTest() {

        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);

        Optional<BookpubOrder> result = orderRepository.findById(order.getOrderNo());

        assertThat(result).isPresent();
        assertThat(result.get().getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(
                order.getMember().getMemberId());
        assertThat(result.get().getDeliveryPricePolicy().getPolicyNo()).isEqualTo(
                order.getDeliveryPricePolicy().getPolicyNo());
        assertThat(result.get().getDeliveryPricePolicy().getPolicyName()).isEqualTo(
                order.getDeliveryPricePolicy().getPolicyName());
        assertThat(result.get().getAddress().getAddressNo()).isEqualTo(
                order.getAddress().getAddressNo());
        assertThat(result.get().getOrderStateCode().getCodeNo()).isEqualTo(
                order.getOrderStateCode().getCodeNo());
        assertThat(result.get().getOrderedAt()).isEqualTo(order.getOrderedAt());
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

    }

}