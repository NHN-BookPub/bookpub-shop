package com.nhnacademy.bookpubshop.product.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

/**
 * 상품 생성시 사용하는 Dto 클래스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateProductRequestDto {
    @NotNull
    @Length(min = 10, max = 13, message = "ISBN은 10자 혹은 13자입니다.")
    private String productIsbn;
    @NotNull
    @Length(max = 100, message = "제목은 최대 100자입니다.")
    private String title;
    @NotNull
    @Length(max = 50, message = "50자를 넘을 수 없습니다.")
    private String productPublisher;
    @NotNull
    private Integer pageCount;
    @NotNull
    @Length(max = 2000, message = "설명은 최대 2000자입니다.")
    private String productDescription;
    @NotNull
    @Length(max = 200)
    private String thumbnailPath;
    @Length(max = 200)
    private String ebookPath;
    @NotNull
    private Long salePrice;
    @NotNull
    private Long productPrice;
    @NotNull
    @Size(min = -10, max = 10, message = "우선순위는 -10 ~ 10 으로 제한됩니다.")
    private int productPriority;
    @NotNull
    private Integer productStock;
    @NotNull
    private LocalDateTime publishedAt;
    @NotNull
    private boolean subscribed;
    @NotNull
    private Integer productPolicyNo;
    @NotNull
    private Integer saleCodeNo;
    @NotNull
    private Integer typeCodeNo;
    @NotBlank
    private String[] authorNos;
    @NotNull
    private List<Long> relationProducts;
}
