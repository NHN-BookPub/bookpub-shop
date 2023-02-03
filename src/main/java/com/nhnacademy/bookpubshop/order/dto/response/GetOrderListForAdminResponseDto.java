package com.nhnacademy.bookpubshop.order.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 관리자용 주문리스트 조회 dto입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class GetOrderListForAdminResponseDto {
    private Long orderNo;
    private String memberId;
    private LocalDateTime createdAt;
    private String invoiceNo;
    private String orderState;
    private Long totalAmount;
    private LocalDateTime receivedAt;
}
