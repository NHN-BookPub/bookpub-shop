package com.nhnacademy.bookpubshop.card.repository;

import com.nhnacademy.bookpubshop.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카드 레포지토리.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public interface CardRepository extends JpaRepository<Card, Long> {

}
