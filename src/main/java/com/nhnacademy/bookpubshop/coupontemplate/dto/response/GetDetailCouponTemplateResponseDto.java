package com.nhnacademy.bookpubshop.coupontemplate.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰템플릿 자세한 정보 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailCouponTemplateResponseDto {
    private Long templateNo;
    private boolean policyFixed;
    private Long policyPrice;
    private Long policyMinimum;
    private Long maxDiscount;
    private String typeName;
    private String productTitle;
    private String categoryName;
    private String codeTarget;
    private String templateName;
    private String templateImage;
    private LocalDateTime finishedAt;
    private boolean templateBundled;

    /**
     * 쿠폰템플릿 dto를 이용하여 이미지 파일을 담아 조회할 수 있도록 변환해주는 메소드입니다.
     * 로컬 저장소 사용시에만 사용.
     *
     * @param templateImage 쿠폰템플릿 이미지 파일(byte)
     * @return 최종 쿠폰템플릿 Dto.
     */
    public GetDetailCouponTemplateResponseDto transform(String templateImage) {
        return new GetDetailCouponTemplateResponseDto(
                this.templateNo,
                this.policyFixed,
                this.policyPrice,
                this.policyMinimum,
                this.maxDiscount,
                this.typeName,
                this.productTitle,
                this.categoryName,
                this.codeTarget,
                this.templateName,
                templateImage,
                this.finishedAt,
                this.templateBundled
        );
    }
}
