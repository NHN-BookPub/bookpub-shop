package com.nhnacademy.bookpubshop.coupontemplate.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 쿠폰템플릿 등록을 위한 DTO 객체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateCouponTemplateRequestDto {
    @NotNull(message = "정책번호를 기입해주세요.")
    private Integer policyNo;

    @NotNull(message = "유형번호를 기입해주세요.")
    private Long typeNo;

    private Long productNo;

    private Integer categoryNo;

    @NotNull(message = "상태번호를 기입해주세요.")
    private Integer codeNo;

    @NotBlank(message = "쿠폰이름을 기입해주세요.")
    @Length(max = 50, message = "쿠폰이름의 최대 글자는 50글자입니다.")
    private String templateName;

    //이미지 관련 이슈때문에 주석처리함. 해결해야함.
    //private MultipartFile templateImage;

    @DateTimeFormat
    private LocalDateTime finishedAt;

    @NotNull
    @DateTimeFormat
    private LocalDateTime issuedAt;

    @NotNull
    private boolean templateOverlapped;

    private boolean templateBundled;
}
