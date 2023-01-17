package com.nhnacademy.bookpubshop.couponmonth.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 이달의쿠폰 등록을 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCouponMonthRequestDto {
    @NotNull(message = "쿠폰 템플릿 번호를 입력하세요.")
    private Long templateNo;

    @NotNull(message = "쿠폰 오픈 시간을 입력하세요.")
    @DateTimeFormat
    private LocalDateTime openedAt;

    @NotNull(message = "수량을 입력하세요.")
    @PositiveOrZero(message = "0이상의 수량을 입력하세요.")
    private Integer monthQuantity;
}
