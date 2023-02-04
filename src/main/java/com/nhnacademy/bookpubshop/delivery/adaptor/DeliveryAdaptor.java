package com.nhnacademy.bookpubshop.delivery.adaptor;

import com.nhnacademy.bookpubshop.delivery.dto.request.CreateDeliveryRequestDto;
import com.nhnacademy.bookpubshop.delivery.dto.response.CreateDeliveryResponseDto;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface DeliveryAdaptor {

    /**
     * 배송을 생성하기위한 메서드입니다.
     *
     * @param dto 배송정보
     * @return uuid 값 반환
     */
    CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto dto);
}
