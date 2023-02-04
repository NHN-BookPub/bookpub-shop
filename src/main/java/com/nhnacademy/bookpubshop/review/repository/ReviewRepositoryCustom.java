package com.nhnacademy.bookpubshop.review.repository;

import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@NoRepositoryBean
public interface ReviewRepositoryCustom {
    Page<GetProductReviewResponseDto> findProductReviews(Pageable pageable, Long productNo);

    Page<GetMemberReviewResponseDto> findMemberReviews(Pageable pageable, Long memberNo);

    Page<GetProductSimpleResponseDto> findWritableMemberReviews(Pageable pageable, Long memberNo);

    Optional<GetMemberReviewResponseDto> findReview(Long reviewNo);

    Optional<GetProductReviewInfoResponseDto> findReviewInfoByProductNo(Long productNo);
}
