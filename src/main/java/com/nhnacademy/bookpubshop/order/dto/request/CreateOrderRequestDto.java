package com.nhnacademy.bookpubshop.order.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 주문등록을 위한 Dto.
 *
 * @author : 여운석, 임태원
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {
    @NotNull(message = "상품 번호 필수 입력사항입니다.")
    private List<Long> productNos;
    @NotNull(message = "상품 별 구매정보는 필수입니다")
    private Map<Long, Integer> productCount;
    @NotNull(message = "상품 별 가격정보는 필수입니다.")
    private Map<Long, Long> productAmount;
    @NotNull(message = "상품 별 구매정보는 필수입니다")
    private Map<Long, Long> productCoupon;
    @NotNull(message = "상품 별 구매정보는 필수입니다")
    private Map<Long, Long> productSaleAmount;
    @NotNull(message = "상품 별 포인트적립금 정보는 필수입니다")
    private Map<Long, Long> productPointSave;
    private Long memberNo;
    @NotNull(message = "배송정책 정보는 필수입니다")
    private Integer deliveryFeePolicyNo;
    @NotNull(message = "포장정책 정보는 필수입니다")
    private Integer packingFeePolicyNo;
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
    @NotNull(message = "수령 날짜는 필수 입력사항입니다.")
    private LocalDateTime receivedAt;
    private boolean packaged;
    private String orderRequest;
    private Long pointAmount;
    private Long couponAmount;
    private Long totalAmount;
    private Long savePoint;
    @NotNull(message = "주문명은 필수 사항입니다")
    private String orderName;
}
