package com.nhnacademy.bookpubshop.coupon.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 쿠폰 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCouponResponseDto {
    private Long couponNo;
    private String memberId;
    private String templateName;
    private String templateImage;
    private boolean policyFixed;
    private Long policyPrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private LocalDateTime finishedAt;
    private boolean couponUsed;

    /**
     * 쿠폰 조회시 이미지를 담기 위해 변환해주는 메소드입니다.
     *
     * @param templateImage 이미지 byte
     * @return 이미지를 담고 있는 dto
     */
    public GetCouponResponseDto transform(String templateImage) {
        return new GetCouponResponseDto(
                this.couponNo,
                this.memberId,
                this.templateName,
                templateImage,
                this.policyFixed,
                this.policyPrice,
                this.policyMinimum,
                this.maxDiscount,
                this.finishedAt,
                this.couponUsed
        );
    }
}
