package com.nhnacademy.bookpubshop.subscribe.dto.request;

import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독생성을 위한 dto 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateSubscribeRequestDto {
    @NotNull
    private String name;

    private Long salePrice;
    private Long price;
    private Integer salesRate;
    private Long viewCount;
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
                .viewCount(this.viewCount)
                .renewed(renewed)
                .build();
    }
}
