package com.nhnacademy.bookpubshop.payment.repository;

import com.nhnacademy.bookpubshop.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 결제를 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 김서현, 임태원
 * @since : 1.0
 **/
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom{

}
