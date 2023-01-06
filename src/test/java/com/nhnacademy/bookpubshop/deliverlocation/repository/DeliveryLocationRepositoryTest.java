package com.nhnacademy.bookpubshop.deliverlocation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.deliverlocation.dummy.DeliveryLocationDummy;
import com.nhnacademy.bookpubshop.deliverlocation.entity.DeliveryLocation;
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
 * 배송위치 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class DeliveryLocationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DeliveryLocationRepository deliveryLocationRepository;

    DeliveryLocation deliveryLocation;

    Delivery delivery;

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
        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, address, orderStateCode);
        delivery = DeliveryDummy.dummy(order);
        deliveryLocation = DeliveryLocationDummy.dummy(delivery);
    }

    @Test
    @DisplayName("배송위치 save 테스트")
    void deliveryLocationSaveTest() {
        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);
        entityManager.persist(delivery);
        entityManager.persist(deliveryLocation);

        Optional<DeliveryLocation> result = deliveryLocationRepository.findById(
                deliveryLocation.getLocationNo());

        assertThat(result).isPresent();
        assertThat(result.get().getLocationNo()).isEqualTo(deliveryLocation.getLocationNo());
        assertThat(result.get().getDelivery().getDeliveryNo()).isEqualTo(
                deliveryLocation.getDelivery().getDeliveryNo());
        assertThat(result.get().getLocationName()).isEqualTo(deliveryLocation.getLocationName());
        assertThat(result.get().getCreatedAt()).isEqualTo(deliveryLocation.getCreatedAt());
    }

}