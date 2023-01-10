package com.nhnacademy.bookpubshop.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.payment.dummy.PaymentDummy;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
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
 * 결제 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    BookpubOrder order;

    BookPubTier bookPubTier;
    Member member;

    PricePolicy pricePolicy;
    PricePolicy packagePricePolicy;
    Address address;
    OrderStateCode orderStateCode;

    PaymentStateCode paymentStateCode;

    PaymentTypeStateCode paymentTypeStateCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        pricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        address = AddressDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, address, orderStateCode);
        paymentStateCode = PaymentStateCodeDummy.dummy();
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
        payment = PaymentDummy.dummy(order, paymentStateCode, paymentTypeStateCode);

    }

    @Test
    @DisplayName("결제 save 테스트")
    void paymentSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);
        entityManager.persist(paymentStateCode);
        entityManager.persist(paymentTypeStateCode);
        entityManager.persist(payment);

        Optional<Payment> result = paymentRepository.findById(payment.getPaymentNo());

        assertThat(result).isPresent();
        assertThat(result.get().getOrder().getOrderNo()).isEqualTo(payment.getOrder().getOrderNo());
        assertThat(result.get().getPaymentStateCode().getCodeNo()).isEqualTo(
                payment.getPaymentStateCode().getCodeNo());
        assertThat(result.get().getPaymentTypeStateCode().getCodeNo()).isEqualTo(
                payment.getPaymentTypeStateCode().getCodeNo());
        assertThat(result.get().getCreatedAt()).isAfter(now);
    }

}