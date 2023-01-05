package com.nhnacademy.bookpubshop.paymentstatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 결제상태코드 Repo Test
 *
 * @author : 정유진
 * @since : 1.0
 **/
@DataJpaTest
class PaymentStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PaymentStateCodeRepository paymentStateCodeRepository;

    PaymentStateCode paymentStateCode;

    @BeforeEach
    void setUp() {
        paymentStateCode = PaymentStateCodeDummy.dummy();
    }

    @Test
    @DisplayName(value = "결제상태코드 save 테스트")
    void paymentStateCodeSaveTest() {
        entityManager.persist(paymentStateCode);
        entityManager.clear();

        Optional<PaymentStateCode> result = paymentStateCodeRepository.findById(paymentStateCode.getCodeNo());
        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentStateCode.getCodeInfo());
    }
}