package com.nhnacademy.bookpubshop.couponmonth.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 이달의쿠폰 수정을 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCouponMonthRequestDto {
    @NotNull(message = "수정할 이달의 쿠폰 번호를 입력하세요.")
    private Long monthNo;

    @NotNull(message = "쿠폰 오픈 시간을 입력하세요.")
    @DateTimeFormat
    private LocalDateTime openedAt;

    @NotNull(message = "수량을 입력하세요.")
    @PositiveOrZero(message = "0이상의 수량을 입력하세요.")
    private Integer monthQuantity;
}
