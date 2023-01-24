package com.nhnacademy.bookpubshop.coupontemplate.dto.request;

import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.product.entity.Product;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime finishedAt;

    private boolean templateBundled;


    /**
     * 쿠폰템플릿 엔티티를 만들어주는 메소드입니다.
     *
     * @param policy      the policy
     * @param couponType  the coupon type
     * @param product     the product
     * @param category    the category
     * @param couponState the coupon state
     * @return the coupon template
     */
    public CouponTemplate createCouponTemplate(CouponPolicy policy,
                                               CouponType couponType,
                                               Product product,
                                               Category category,
                                               CouponStateCode couponState) {
        return CouponTemplate.builder()
                .couponPolicy(policy)
                .couponType(couponType)
                .product(product)
                .couponStateCode(couponState)
                .category(category)
                .templateName(this.templateName)
                .finishedAt(this.finishedAt)
                .templateBundled(this.templateBundled)
                .build();
    }
}
