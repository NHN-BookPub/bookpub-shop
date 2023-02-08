package com.nhnacademy.bookpubshop.paymenttypestatecode.repository;

import com.nhnacademy.bookpubshop.paymenttypestatecode.dto.response.GetPaymentTypeResponseDto;
import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 결제유형코드 레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PaymentTypeRepositoryCustom {
    /**
     * 모든 결제유형을 가져오는 메소드.
     *
     * @return 전체 결제유형.
     */
    List<GetPaymentTypeResponseDto> getAllPaymentType();

    /**
     * 결제유형 이름으로 검색하여 가져오는 메소드.
     *
     * @return 찾고자하는 결제유형.
     */
    Optional<PaymentTypeStateCode> getPaymentType(String type);
}
