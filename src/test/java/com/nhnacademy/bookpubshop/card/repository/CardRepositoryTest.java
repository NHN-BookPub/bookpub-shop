package com.nhnacademy.bookpubshop.card.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.card.dummy.CardDummy;
import com.nhnacademy.bookpubshop.card.entity.Card;
import com.nhnacademy.bookpubshop.cardstatecode.dummy.CardStateCodeDummy;
import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 카드 Repo Test
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class CardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardRepository cardRepository;

    BookPubTier bookPubTier;
    Member member;
    PricePolicy pricePolicy;
    PricePolicy packagePricePolicy;
    Address address;
    OrderStateCode orderStateCode;
    PaymentStateCode paymentStateCode;
    PaymentTypeStateCode paymentTypeStateCode;
    CardStateCode cardStateCode;
    BookpubOrder order;
    Payment payment;
    Card card;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        pricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        address = AddressDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        paymentStateCode = PaymentStateCodeDummy.dummy();
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
        cardStateCode = CardStateCodeDummy.dummy();
        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, address, orderStateCode);
        payment = PaymentDummy.dummy(order, paymentStateCode, paymentTypeStateCode);
    }

    @Test
    @DisplayName(value = "카드 save 테스트")
    void cardSaveTest() {
        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        entityManager.persist(paymentStateCode);
        entityManager.persist(paymentTypeStateCode);
        entityManager.persist(cardStateCode);
        entityManager.persist(order);
        Payment persist = entityManager.persist(payment);
        card = CardDummy.dummy(persist, cardStateCode);
        Card testCard = entityManager.persist(card);

        Optional<Card> result = cardRepository.findById(testCard.getPaymentNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPaymentNo()).isEqualTo(testCard.getPaymentNo());
        assertThat(result.get().getPayment().getPaymentNo()).isEqualTo(testCard.getPayment().getPaymentNo());
        assertThat(result.get().getCardStateCode().getCodeNo()).isEqualTo(testCard.getCardStateCode().getCodeNo());
        assertThat(result.get().getCardCompany()).isEqualTo(testCard.getCardCompany());
        assertThat(result.get().getCardNo()).isEqualTo(testCard.getCardNo());
        assertThat(result.get().isCodeSucceed()).isTrue();
        assertThat(result.get().getInstallmentMonth()).isEqualTo(testCard.getInstallmentMonth());
    }
}