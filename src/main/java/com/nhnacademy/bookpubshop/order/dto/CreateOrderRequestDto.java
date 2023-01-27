package com.nhnacademy.bookpubshop.order.dto;

import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 주문등록을 위한 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {

    @NotBlank(message = "상품 번호 필수 입력사항입니다.")
    private List<Long> productNos;
    private Map<Long, Integer> productAmounts;
    private Map<Long, Long> productCouponAmounts;
    private Map<Long, String> orderProductReasons;
    @StateCode(enumClass = OrderState.class)
    private String orderState;
    @NotBlank(message = "구매자 명은 필수 입력사항입니다.")
    @Length(max = 200, message = "구매자명은 최대 200글자 가능합니다.")
    private String buyerName;
    @NotBlank(message = "구매자 번호는 필수 입력사항입니다.")
    @Length(max = 20, message = "구매자 번호는 최대 20글자 가능합니다.")
    private String buyerNumber;
    @NotBlank(message = "수령인은 필수 입력사항입니다.")
    @Length(max = 200, message = "수령인 이름은 최대 200글자 가능합니다.")
    private String recipientName;
    @NotBlank(message = "수령인 번호는 필수 입력사항입니다.")
    @Length(max = 20, message = "수령인 번호는 최대 20글자 가능합니다.")
    private String recipientNumber;
    @NotBlank(message = "상세주소는 필수 입력사항입니다.")
    @Length(max = 100, message = "상세주소는 최대 100글자 가능합니다.")
    private String addressDetail;
    @NotBlank(message = "도로명 주소는 필수 입력사항입니다.")
    @Length(max = 100, message = "도로명 주소는 최대100글자 가능합니다.")
    private String roadAddress;
    @NotBlank(message = "수량 날짜는 필수 입력사항입니다.")
    private LocalDateTime receivedAt;
    private boolean packaged;
    private String orderRequest;
    private Long pointAmount;
    private Long couponAmount;
    private Long totalAmount;
}
