package com.nhnacademy.bookpubshop.subscribe.dto.request;

import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 구독생성을 위한 dto 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateSubscribeRequestDto {
    @Length(max = 20, message = "이름의 길이가 너무 깁니다.")
    @NotBlank(message = "구독이름은 비어있을수 업습니다.")
    private String name;
    @Min(value = 0, message = "할인 가격이 0 이하로 내려갈순 없습니다.")
    @NotNull
    private Long salePrice;
    @Min(value = 0, message = "가격이 0 이하로 내려갈순 없습니다.")
    @NotNull
    private Long price;
    @Min(value = 0, message = "할인률은 최소 0입니다.")
    @Max(value = 100, message = "할인률은 최대 100입니다.")
    @NotNull
    private Integer salesRate;
    private boolean renewed;

    /**
     * Dto 를 Entity 변환시키기위한 메서드입니다.
     *
     * @return 구독이 반환
     */
    public Subscribe dtoToEntity() {
        return Subscribe.builder()
                .subscribeName(this.name)
                .salesPrice(this.salePrice)
                .subscribePrice(this.price)
                .salesRate(this.salesRate)
                .renewed(renewed)
                .build();
    }
}
