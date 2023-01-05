package com.nhnacademy.bookpubshop.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.delivery.dummy.DeliveryDummy;
import com.nhnacademy.bookpubshop.delivery.entity.Delivery;
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
 * 배송 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class DeliveryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DeliveryRepository deliveryRepository;

    BookpubOrder order;
    Delivery delivery;
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
        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, address, orderStateCode);
        delivery = DeliveryDummy.dummy(order);
    }

    @Test
    @DisplayName("배송 save 테스트")
    void DeliverySaveTest() {
        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);
        entityManager.persist(delivery);
        entityManager.clear();

        Optional<Delivery> result = deliveryRepository.findById(delivery.getDeliveryNo());

        assertThat(result).isPresent();
        assertThat(result.get().getDeliveryNo()).isEqualTo(delivery.getDeliveryNo());
        assertThat(result.get().getOrder().getOrderNo()).isEqualTo(
                delivery.getOrder().getOrderNo());
        assertThat(result.get().getInvoiceNo()).isEqualTo(delivery.getInvoiceNo());
        assertThat(result.get().getDeliveryCompany()).isEqualTo(delivery.getDeliveryCompany());
        assertThat(result.get().getDeliveryState()).isEqualTo(delivery.getDeliveryState());
        assertThat(result.get().getDeliveryRequest()).isEqualTo(delivery.getDeliveryRequest());
        assertThat(result.get().getDeliveryRecipient()).isEqualTo(delivery.getDeliveryRecipient());
        assertThat(result.get().getRecipientPhone()).isEqualTo(delivery.getRecipientPhone());
        assertThat(result.get().getFinishedAt()).isEqualTo(delivery.getFinishedAt());
    }

}