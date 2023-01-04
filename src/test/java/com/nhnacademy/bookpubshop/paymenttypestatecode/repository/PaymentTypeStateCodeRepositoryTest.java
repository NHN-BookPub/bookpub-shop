package com.nhnacademy.bookpubshop.paymenttypestatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 결제유형상태코드 Repo Test
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class PaymentTypeStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PaymentTypeStateCodeRepository paymentTypeStateCodeRepository;

    PaymentTypeStateCode paymentTypeStateCode;

    @BeforeEach
    void setup() {
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
    }

    @Test
    @DisplayName(value = "결제유형상태코드 save 테스트")
    void paymentTypeStateCodeSaveTest() {
        entityManager.persist(paymentTypeStateCode);
        entityManager.clear();

        Optional<PaymentTypeStateCode> result = paymentTypeStateCodeRepository.findById(
                paymentTypeStateCode.getCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentTypeStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentTypeStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentTypeStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentTypeStateCode.getCodeInfo());

    }

}