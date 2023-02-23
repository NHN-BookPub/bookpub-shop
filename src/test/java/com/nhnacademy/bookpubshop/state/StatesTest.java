package com.nhnacademy.bookpubshop.state;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.nhnacademy.bookpubshop.pricepolicy.dto.request.CreatePricePolicyRequestDto;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * validator 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class StatesTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("가격정책 검증 성공")
    void validatePricePolicyTest() {
        CreatePricePolicyRequestDto dto =
                new CreatePricePolicyRequestDto();

        ReflectionTestUtils.setField(dto, "policyName", "배송비");
        ReflectionTestUtils.setField(dto, "policyFee", 1000L);

        Set<ConstraintViolation<CreatePricePolicyRequestDto>> violations
                = validator.validate(dto);

        assertThat(violations).isNotNull();
    }

    @Test
    @DisplayName("가격정책 검증 실패")
    void validatePricePolicyFailedTest() {
        CreatePricePolicyRequestDto dto =
                new CreatePricePolicyRequestDto();

        ReflectionTestUtils.setField(dto, "policyName", "사장용돈");
        ReflectionTestUtils.setField(dto, "policyFee", 1000L);

        Set<ConstraintViolation<CreatePricePolicyRequestDto>> violations
                = validator.validate(dto);

        assertThat(violations).isNotNull();
    }

    @Test
    @DisplayName("주문상태 engName To name 성공")
    void engToNameTest() {
        String name = OrderState.CANCEL.getNameByEngName("canceled");

        assertThat(OrderState.CANCEL.getName()).isEqualTo(name);
    }
}