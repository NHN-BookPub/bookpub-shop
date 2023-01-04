package com.nhnacademy.bookpubshop.card.repository;

import com.nhnacademy.bookpubshop.card.entity.Card;
import com.nhnacademy.bookpubshop.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카드를 데이터베이스에서 다루기위한 Repo 클래스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CardRepository extends JpaRepository<Card, Payment> {
}
