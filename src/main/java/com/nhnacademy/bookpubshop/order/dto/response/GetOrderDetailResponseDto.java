package com.nhnacademy.bookpubshop.order.dto.response;

import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * 주문 상세정보를 반환하기 위한 Dto.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
public class GetOrderDetailResponseDto {
    private Long orderNo;
    private Long memberNo;
    private List<GetProductListForOrderResponseDto> orderProducts = new ArrayList<>();
    private String orderState;
    private String buyerName;
    private String buyerNumber;
    private String recipientName;
    private String recipientNumber;
    private String addressBase;
    private String addressDetail;
    private LocalDateTime createdAt;
    private LocalDateTime receivedAt;
    private String invoiceNo;
    private boolean packaged;
    private Long packageAmount;
    private Long deliveryAmount;
    private String orderRequest;
    private Long pointAmount;
    private Long couponAmount;
    private Long totalAmount;
    private String orderName;
    private String orderId;

    public GetOrderDetailResponseDto(Long orderNo,
                                     String orderState,
                                     String buyerName,
                                     String buyerNumber,
                                     String recipientName,
                                     String recipientNumber,
                                     String addressBase,
                                     String addressDetail,
                                     LocalDateTime createdAt,
                                     LocalDateTime receivedAt,
                                     String invoiceNo,
                                     boolean packaged,
                                     Long packageAmount,
                                     Long deliveryAmount,
                                     String orderRequest,
                                     Long pointAmount,
                                     Long couponAmount,
                                     Long totalAmount,
                                     String orderName,
                                     String orderId) {
        this.orderNo = orderNo;
        this.orderState = orderState;
        this.buyerName = buyerName;
        this.buyerNumber = buyerNumber;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.createdAt = createdAt;
        this.receivedAt = receivedAt;
        this.invoiceNo = invoiceNo;
        this.packaged = packaged;
        this.packageAmount = packageAmount;
        this.deliveryAmount = deliveryAmount;
        this.orderRequest = orderRequest;
        this.pointAmount = pointAmount;
        this.couponAmount = couponAmount;
        this.totalAmount = totalAmount;
        this.orderName = orderName;
        this.orderId = orderId;
    }

    public GetOrderDetailResponseDto(Long orderNo,
                                     Long memberNo,
                                     String orderState,
                                     String buyerName,
                                     String buyerNumber,
                                     String recipientName,
                                     String recipientNumber,
                                     String addressBase,
                                     String addressDetail,
                                     LocalDateTime createdAt,
                                     LocalDateTime receivedAt,
                                     String invoiceNo,
                                     boolean packaged,
                                     Long packageAmount,
                                     Long deliveryAmount,
                                     String orderRequest,
                                     Long pointAmount,
                                     Long couponAmount,
                                     Long totalAmount,
                                     String orderName,
                                     String orderId) {
        this.orderNo = orderNo;
        this.memberNo = memberNo;
        this.orderState = orderState;
        this.buyerName = buyerName;
        this.buyerNumber = buyerNumber;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
        this.createdAt = createdAt;
        this.receivedAt = receivedAt;
        this.invoiceNo = invoiceNo;
        this.packaged = packaged;
        this.packageAmount = packageAmount;
        this.deliveryAmount = deliveryAmount;
        this.orderRequest = orderRequest;
        this.pointAmount = pointAmount;
        this.couponAmount = couponAmount;
        this.totalAmount = totalAmount;
        this.orderName = orderName;
        this.orderId = orderId;
    }

    public void addProducts(List<GetProductListForOrderResponseDto> products) {
        this.orderProducts = products;
    }
}
