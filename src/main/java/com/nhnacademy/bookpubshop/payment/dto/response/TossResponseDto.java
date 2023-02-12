package com.nhnacademy.bookpubshop.payment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 토스 결제승인 시 반환되는 response dto.
 *
 * @author 임태원
 * @since 1.0
 */
@Getter
@Setter
public class TossResponseDto {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String type;
    private Failure failure;
    private Receipt receipt;
    private Long totalAmount;
    private String method;
    private Card card;
    private Cancel[] cancels;
    private EasyPay easyPay;

    /**
     * 영수증 정보가 담겨있습니다.
     */
    @Getter
    @Setter
    public static class Receipt {
        private String url;
    }

    /**
     * 카드 결제 시 카드결제의 정보가 담겨있습니다.
     */
    @Getter
    @Setter
    public static class Card {
        private String issuerCode;
        private String number;
        private Integer installmentPlanMonths;
    }

    /**
     * 간편 결제 시 간편결제의 정보가 담겨있습니다.
     */
    @Getter
    @Setter
    public static class EasyPay {
        private Long amount;
        private String provider;
        private Number discountAmount;
    }

    /**
     * 결제 실패에 대한 정보가 담겨있습니다.
     */
    @Getter
    @Setter
    public static class Failure {
        private String code;
        private String message;
    }

    /**
     * 결제 취소에 대한 정보가 담겨있습니다.
     */
    @Getter
    @Setter
    public static class Cancel {
        private String cancelReason;
        private String canceledAt;
        private Long cancelAmount;
        private Long taxFreeAmount;
        private Long taxExemptionAmount;
        private Long refundableAmount;
        private Long easyPayDiscountAmount;
    }
}
