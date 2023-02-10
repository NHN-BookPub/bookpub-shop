package com.nhnacademy.bookpubshop.delivery.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  배송 생성을 위한 request 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class CreateDeliveryRequestDto {
    @NotNull
    private Long orderNo;
    @NotBlank
    private String deliveryRequest;
    @NotBlank
    private String recipient;
    @NotBlank
    private String phone;
    @NotNull
    private LocalDateTime requestDate;
    @NotNull
    private String addressDetail;
}
