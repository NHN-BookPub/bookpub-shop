package com.nhnacademy.bookpubshop.review.repository;

import com.nhnacademy.bookpubshop.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품평 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

}
