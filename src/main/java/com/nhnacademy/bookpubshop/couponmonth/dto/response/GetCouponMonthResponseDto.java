package com.nhnacademy.bookpubshop.couponmonth.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이달의쿠폰 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCouponMonthResponseDto {
    private Long monthNo;
    private Long templateNo;
    private String templateName;
    private String templateImage;
    private LocalDateTime openedAt;
    private Integer monthQuantity;

    /**
     * 쿠폰 조회시 이미지를 가져오기 위해 변환시켜주는 dto 입니다.
     *
     * @param templateImage 이미지 byte
     * @return 이미지를 들고있는 dto
     */
    public GetCouponMonthResponseDto transform(String templateImage) {
        return new GetCouponMonthResponseDto(
                this.monthNo,
                this.templateNo,
                this.templateName,
                templateImage,
                this.openedAt,
                this.monthQuantity
        );
    }
}
