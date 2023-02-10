package com.nhnacademy.bookpubshop.subscribe.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독에 상품연관관계를 추가할때 쓰임.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateSubscribeProductRequestDto {
    @NotNull(message = "상품 번호들은 비어있을수 없습니다.")
    private List<Long> productNo;
}
