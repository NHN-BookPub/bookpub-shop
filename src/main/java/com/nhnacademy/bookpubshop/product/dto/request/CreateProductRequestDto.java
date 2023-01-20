package com.nhnacademy.bookpubshop.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 상품 생성시 사용하는 Dto 클래스.
 *
 * @author : 여운석, 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateProductRequestDto {
    @NotNull
    @Length(min = 10, max = 13, message = "ISBN은 10자 혹은 13자입니다.")
    private String productIsbn;
    @Length(min = 1, max = 100, message = "제목은 최대 100자입니다.")
    private String title;
    @NotNull
    @Length(max = 50, message = "50자를 넘을 수 없습니다.")
    private String productPublisher;
    @NotNull
    private Integer pageCount;
    @Length(min = 1, max = 2000, message = "설명은 최대 2000자입니다.")
    private String productDescription;
    @NotNull
    private Long salePrice;
    @NotNull
    private Long productPrice;
    @NotNull
    private Integer salesRate;
    @NotNull
    private Integer productPriority;
    @NotNull
    private Integer productStock;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime publishedAt;
    @NotNull
    private boolean subscribed;
    @NotNull
    private Integer productPolicyNo;
    @NotNull
    private Integer saleCodeNo;
    @NotNull
    private Integer typeCodeNo;
    private List<Integer> authorsNo;
    private List<Integer> categoriesNo;
    private List<Integer> tagsNo;

    private List<Long> relationProducts;
}
