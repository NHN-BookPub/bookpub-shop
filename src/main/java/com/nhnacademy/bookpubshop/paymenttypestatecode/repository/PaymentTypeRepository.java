package com.nhnacademy.bookpubshop.paymenttypestatecode.repository;

import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 결제유형상태코드를 데이터베이스에서 다루기위한 Repo 클래스.
 *
 * @author : 김서현, 임태원
 * @since : 1.0
 **/
public interface PaymentTypeRepository extends
        JpaRepository<PaymentTypeStateCode, Integer>, PaymentTypeRepositoryCustom {


}
