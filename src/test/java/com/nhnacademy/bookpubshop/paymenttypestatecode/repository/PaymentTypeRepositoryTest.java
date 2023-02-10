package com.nhnacademy.bookpubshop.paymenttypestatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.dummy.PaymentTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.util.List;
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
class PaymentTypeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    PaymentTypeStateCode paymentTypeStateCode;

    @BeforeEach
    void setUp() {
        paymentTypeStateCode = PaymentTypeStateCodeDummy.dummy();
        entityManager.persist(paymentTypeStateCode);

    }

    @Test
    @DisplayName(value = "결제유형상태코드 save 테스트")
    void paymentTypeStateCodeSaveTest() {
        Optional<PaymentTypeStateCode> result = paymentTypeRepository.findById(
                paymentTypeStateCode.getCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentTypeStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentTypeStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentTypeStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentTypeStateCode.getCodeInfo());
    }

    @Test
    @DisplayName(value = "전체 결제유형상태코드 반환 테스트")
    void getAllPaymentType() {
        List<GetPaymentTypeResponseDto> result
                = paymentTypeRepository.getAllPaymentType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodeNo()).isEqualTo(paymentTypeStateCode.getCodeNo());
        assertThat(result.get(0).getCodeName()).isEqualTo(paymentTypeStateCode.getCodeName());
        assertThat(result.get(0).isCodeUsed()).isEqualTo(paymentTypeStateCode.isCodeUsed());
        assertThat(result.get(0).getCodeInfo()).isEqualTo(paymentTypeStateCode.getCodeInfo());
    }

    @Test
    @DisplayName(value = "결제유형상태코드 타입명으로 검색 테스트")
    void getPaymentType() {
        Optional<PaymentTypeStateCode> result = paymentTypeRepository.getPaymentType(
                paymentTypeStateCode.getCodeName());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentTypeStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentTypeStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentTypeStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentTypeStateCode.getCodeInfo());
    }

}