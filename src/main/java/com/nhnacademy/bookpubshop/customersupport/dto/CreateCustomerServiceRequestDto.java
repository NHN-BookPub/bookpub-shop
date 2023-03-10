package com.nhnacademy.bookpubshop.customersupport.dto;

import com.nhnacademy.bookpubshop.state.CustomerServiceCategory;
import com.nhnacademy.bookpubshop.state.CustomerServiceState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 고객서비스 생성을 위한 dto 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCustomerServiceRequestDto {
    @StateCode(enumClass = CustomerServiceState.class)
    private String customerServiceStateCode;
    @NotNull
    private Long memberNo;
    @StateCode(enumClass = CustomerServiceCategory.class)
    private String serviceCategory;
    @Length(min = 1, max = 100, message = "100자를 넘을 수 없습니다.")
    private String serviceTitle;
    @Length(min = 1, max = 5000, message = "5000자를 넘을 수 없습니다.")
    private String serviceContent;
}
