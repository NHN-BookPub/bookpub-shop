package com.nhnacademy.bookpubshop.coupontemplate.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰템플릿 조회를 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCouponTemplateResponseDto {
    private Long templateNo;
    private String templateName;
    private String templateImage;
    private LocalDateTime finishedAt;

    /**
     * 쿠폰템플릿 dto를 이용하여 이미지 파일을 담아 조회할 수 있도록 변환해주는 메소드입니다.
     * 로컬 저장소 사용시에만 사용.
     *
     * @param templateImage 쿠폰템플릿 이미지 파일(byte)
     * @return 최종 쿠폰템플릿 Dto.
     */
    public GetCouponTemplateResponseDto transform(String templateImage) {
        return new GetCouponTemplateResponseDto(
                this.templateNo,
                this.templateName,
                templateImage,
                this.finishedAt
        );
    }
}
