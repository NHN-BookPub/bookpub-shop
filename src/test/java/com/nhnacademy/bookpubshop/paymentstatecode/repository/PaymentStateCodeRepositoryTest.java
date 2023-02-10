package com.nhnacademy.bookpubshop.paymentstatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.paymentstatecode.dto.response.GetPaymentStateResponseDto;
import com.nhnacademy.bookpubshop.paymentstatecode.dummy.PaymentStateCodeDummy;
import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import java.util.List;
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
    GetPaymentStateResponseDto paymentStateResponseDto;

    @BeforeEach
    void setUp() {
        paymentStateCode = PaymentStateCodeDummy.dummy();
        entityManager.persist(paymentStateCode);
        paymentStateResponseDto = new GetPaymentStateResponseDto(
                paymentStateCode.getCodeNo(),
                paymentStateCode.getCodeName(),
                paymentStateCode.isCodeUsed(),
                paymentStateCode.getCodeInfo()
        );


    }

    @Test
    @DisplayName(value = "결제상태코드 save 테스트")
    void paymentStateCodeSaveTest() {
        Optional<PaymentStateCode> result = paymentStateCodeRepository.findById(paymentStateCode.getCodeNo());
        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentStateCode.getCodeInfo());
    }

    @Test
    @DisplayName(value = "전체 결제유형상태코드 반환 테스트")
    void getAllPaymentType() {
        List<GetPaymentStateResponseDto> result
                = paymentStateCodeRepository.getAllPaymentState();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCodeNo()).isEqualTo(paymentStateResponseDto.getCodeNo());
        assertThat(result.get(0).getCodeName()).isEqualTo(paymentStateResponseDto.getCodeName());
        assertThat(result.get(0).isCodeUsed()).isEqualTo(paymentStateResponseDto.isCodeUsed());
        assertThat(result.get(0).getCodeInfo()).isEqualTo(paymentStateResponseDto.getCodeInfo());
    }

    @Test
    @DisplayName(value = "결제유형상태코드 타입명으로 검색 테스트")
    void getPaymentType() {
        Optional<PaymentStateCode> result = paymentStateCodeRepository.getPaymentStateCode(
                paymentStateCode.getCodeName());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(paymentStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(paymentStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(paymentStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(paymentStateCode.getCodeInfo());
    }
}