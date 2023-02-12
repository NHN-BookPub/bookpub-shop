package com.nhnacademy.bookpubshop.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.payment.dto.request.RefundRequestDto;
import com.nhnacademy.bookpubshop.payment.dto.response.GetRefundResponseDto;
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
    OrderStateCode orderStateCode;
    PaymentStateCode paymentStateCode;
    PaymentTypeStateCode paymentTypeStateCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        pricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        order = OrderDummy.dummy(member, pricePolicy, packagePricePolicy, orderStateCode);
        paymentStateCode = PaymentStateCodeDummy.dummy();
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
        payment = PaymentDummy.dummy(order, paymentStateCode, paymentTypeStateCode);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(orderStateCode);
        entityManager.persist(order);
        entityManager.persist(paymentStateCode);
        entityManager.persist(paymentTypeStateCode);
        entityManager.persist(payment);

    }

    @Test
    @DisplayName("결제 save 테스트")
    void paymentSaveTest() {

        Optional<Payment> result = paymentRepository.findById(payment.getPaymentNo());

        assertThat(result).isPresent();
        assertThat(result.get().getOrder().getOrderNo()).isEqualTo(payment.getOrder().getOrderNo());
        assertThat(result.get().getPaymentStateCode().getCodeNo()).isEqualTo(
                payment.getPaymentStateCode().getCodeNo());
        assertThat(result.get().getPaymentTypeStateCode().getCodeNo()).isEqualTo(
                payment.getPaymentTypeStateCode().getCodeNo());
        assertThat(result.get().getCreatedAt()).isEqualTo(payment.getCreatedAt());
    }
    @Test
    @DisplayName("환불정보 확인테스트")
    void paymentRefundTest(){
        RefundRequestDto cancel = new RefundRequestDto(order.getOrderNo(), "cancel");
        Optional<GetRefundResponseDto> refundInfo = paymentRepository.getRefundInfo(cancel);

        assertThat(refundInfo).isPresent();
        assertThat(refundInfo.get().getPaymentKey()).isEqualTo(payment.getPaymentKey());
    }

    @Test
    @DisplayName("결제 정보받기")
    void paymentGet(){
        Optional<Payment> result = paymentRepository.getPayment(payment.getPaymentKey());

        assertThat(result).isPresent();
        assertThat(result.get().getPaymentKey()).isEqualTo(payment.getPaymentKey());
        assertThat(result.get().getPaymentNo()).isEqualTo(payment.getPaymentNo());
        assertThat(result.get().getReceipt()).isEqualTo(payment.getReceipt());
        assertThat(result.get().getApprovedAt()).isEqualTo(payment.getApprovedAt());
        assertThat(result.get().getPaymentCancelReason()).isEqualTo(payment.getPaymentCancelReason());
        assertThat(result.get().getPaymentStateCode()).isEqualTo(payment.getPaymentStateCode());
        assertThat(result.get().getPaymentTypeStateCode()).isEqualTo(payment.getPaymentTypeStateCode());
    }

    @Test
    @DisplayName("결제정보통해 주문받기")
    void paymentOrder(){
        Optional<BookpubOrder> result = paymentRepository.getOrderByPaymentKey(payment.getPaymentKey());

        assertThat(result).isPresent();
        assertThat(result.get().getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(result.get().getMember()).isEqualTo(order.getMember());
        assertThat(result.get().getDeliveryPricePolicy()).isEqualTo(order.getDeliveryPricePolicy());
        assertThat(result.get().getPackagingPricePolicy()).isEqualTo(order.getPackagingPricePolicy());
        assertThat(result.get().getOrderStateCode()).isEqualTo(order.getOrderStateCode());
        assertThat(result.get().getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.get().getOrderRecipient()).isEqualTo(order.getOrderRecipient());
        assertThat(result.get().getOrderBuyer()).isEqualTo(order.getOrderBuyer());
        assertThat(result.get().getBuyerPhone()).isEqualTo(order.getBuyerPhone());
        assertThat(result.get().getReceivedAt()).isEqualTo(order.getReceivedAt());
        assertThat(result.get().getInvoiceNumber()).isEqualTo(order.getInvoiceNumber());
        assertThat(result.get().getOrderPrice()).isEqualTo(order.getOrderPrice());
        assertThat(result.get().getPointAmount()).isEqualTo(order.getPointAmount());
        assertThat(result.get().getPointSave()).isEqualTo(order.getPointSave());
        assertThat(result.get().isOrderPackaged()).isEqualTo(order.isOrderPackaged());
        assertThat(result.get().getOrderRequest()).isEqualTo(order.getOrderRequest());
        assertThat(result.get().getCouponDiscount()).isEqualTo(order.getCouponDiscount());
        assertThat(result.get().getAddressDetail()).isEqualTo(order.getAddressDetail());
        assertThat(result.get().getRoadAddress()).isEqualTo(order.getRoadAddress());
        assertThat(result.get().getOrderName()).isEqualTo(order.getOrderName());
    }
}