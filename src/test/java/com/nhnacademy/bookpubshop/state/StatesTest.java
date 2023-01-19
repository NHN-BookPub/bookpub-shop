package com.nhnacademy.bookpubshop.state;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.nhnacademy.bookpubshop.pricepolicy.dto.CreatePricePolicyRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
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
public class StatesTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    @DisplayName("판매코드 검증 성공")
    void validateSaleCodeTest() {
        CreateProductSaleStateCodeRequestDto dto =
                new CreateProductSaleStateCodeRequestDto();

        ReflectionTestUtils.setField(dto, "codeCategory", "판매중");
        ReflectionTestUtils.setField(dto, "codeUsed", true);
        ReflectionTestUtils.setField(dto, "codeInfo", "test");


        Set<ConstraintViolation<CreateProductSaleStateCodeRequestDto>> violations
                = validator.validate(dto);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("판매코드 검증 실패")
    void validateSaleCodeFailed() {
        CreateProductSaleStateCodeRequestDto dto =
                new CreateProductSaleStateCodeRequestDto();

        ReflectionTestUtils.setField(dto, "codeCategory", "20자보다 더욱더욱더욱 더 많이 긴 값");
        ReflectionTestUtils.setField(dto, "codeUsed", false);
        ReflectionTestUtils.setField(dto, "codeInfo", "test");

        Set<ConstraintViolation<CreateProductSaleStateCodeRequestDto>> violations
                = validator.validate(dto);

        assertThat(violations.isEmpty()).isFalse();
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

        assertThat(violations.isEmpty()).isTrue();
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

        assertThat(violations.isEmpty()).isFalse();
    }
}