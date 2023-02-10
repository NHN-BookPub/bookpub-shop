package com.nhnacademy.bookpubshop.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 상품 수정을 위한 dto.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class ModifyProductInfoRequestDto {

    @NotBlank(message = "ISBN은 필수입니다.")
    @Length(min = 10, max = 13, message = "ISBN은 10자 혹은 13자입니다.")
    private String productIsbn;
    @NotNull(message = "정가는 입력이 필수입니다.")
    private Long productPrice;
    private Integer salesRate;
    @NotBlank(message = "출판사 값 입력은 필수입니다.")
    @Length(max = 50, message = "50자를 넘을 수 없습니다.")
    private String productPublisher;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime publishedAt;
    private Integer pageCount;
    @NotNull(message = "판매가는 필수입니다.")
    private Long salesPrice;
    private Integer priority;
}
