package com.nhnacademy.bookpubshop.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *  배송 생성을 위한 request 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@ToString
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestDate;
    @NotNull
    private String addressDetail;
}
