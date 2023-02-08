package com.nhnacademy.bookpubshop.paymenttypestatecode.service;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.util.List;

/**
 * 결제유형 서비스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PaymentTypeService {

    /**
     * 모든 결제유형을 가져오는 메소드.
     *
     * @return 모든 판매유형 정보.
     */
    List<GetPaymentTypeResponseDto> getAllPaymentType();

    /**
     * 결유형의 이름으로 검색해서 가져오는 메소드.
     *
     * @param type 판매유형 이름.
     * @return 판매유형 정보.
     */
    PaymentTypeStateCode getPaymentType(String type);
}
