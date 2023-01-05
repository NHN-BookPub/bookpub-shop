package com.nhnacademy.bookpubshop.cardstatecode.repository;

import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카드결제상태코드를 데이터베이스에서 다루기위한 Repo 클래스.
 *
 * @author : 김서현
 * @since : 1.0
 **/

public interface CardStateCodeRepository extends JpaRepository<CardStateCode, Integer> {


}
