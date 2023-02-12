package com.nhnacademy.bookpubshop.product.dto.request;

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
    @NotNull(message = "ISBN을 입력해주세요.")
    @Length(min = 10, max = 13, message = "ISBN은 10자 혹은 13자입니다.")
    private String productIsbn;
    @Length(min = 1, max = 100, message = "제목은 최대 100자입니다.")
    private String title;
    @NotNull(message = "출판사를 입력해주세요.")
    @Length(max = 50, message = "50자를 넘을 수 없습니다.")
    private String productPublisher;

    private Integer pageCount;
    @Length(max = 5000, message = "설명은 최대 5000자입니다.")
    private String productDescription;
    @NotNull(message = "판매가를 입력해주세요.")
    private Long salePrice;
    @NotNull(message = "정가를 입력해주세요.")
    private Long productPrice;

    private Integer salesRate;

    private Integer productPriority;
    @NotNull(message = "상품 재고를 입력해주세요.")
    private Integer productStock;
    @NotNull(message = "출판일시를 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime publishedAt;

    private boolean subscribed;
    @NotNull(message = "상품정책 번호를 입력해주세요.")
    private Integer productPolicyNo;
    @NotNull(message = "상품판매여부코드 번호를 입력해주세요.")
    private Integer saleCodeNo;
    @NotNull(message = "상품유형코드 번호를 입력해주세요.")
    private Integer typeCodeNo;
    private List<Integer> authorsNo;
    private List<Integer> categoriesNo;
    private List<Integer> tagsNo;
}
