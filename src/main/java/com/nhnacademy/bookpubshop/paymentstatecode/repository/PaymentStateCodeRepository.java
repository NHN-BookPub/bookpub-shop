package com.nhnacademy.bookpubshop.paymentstatecode.repository;

import com.nhnacademy.bookpubshop.paymentstatecode.entity.PaymentStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 결제상태코드 테이블을 데이터베이스에서 다루기 위한 Repo 클래스.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface PaymentStateCodeRepository
        extends JpaRepository<PaymentStateCode, Integer>, PaymentStateCodeRepositoryCustom {
}
