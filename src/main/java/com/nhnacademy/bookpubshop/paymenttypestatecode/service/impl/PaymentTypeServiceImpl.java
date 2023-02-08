package com.nhnacademy.bookpubshop.paymenttypestatecode.service.impl;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import com.nhnacademy.bookpubshop.paymenttypestatecode.exception.NotFoundPaymentTypeException;
import com.nhnacademy.bookpubshop.paymenttypestatecode.repository.PaymentTypeRepository;
import com.nhnacademy.bookpubshop.paymenttypestatecode.service.PaymentTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 판매유형 서비스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPaymentTypeResponseDto> getAllPaymentType() {
        return paymentTypeRepository.getAllPaymentType();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NotFoundPaymentTypeException 결제타입을 찾을 수 없을 때 발생하는 에러.
     */
    @Override
    public PaymentTypeStateCode getPaymentType(String type) {
        return paymentTypeRepository.getPaymentType(type)
                .orElseThrow(NotFoundPaymentTypeException::new);
    }
}
