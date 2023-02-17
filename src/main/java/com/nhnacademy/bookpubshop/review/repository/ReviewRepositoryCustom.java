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
 * 상품평 custom 레포지토리.
 *
 * @author : 정유진
 * @since : 1.0
 */
@NoRepositoryBean
public interface ReviewRepositoryCustom {
    /**
     * 상품에 작성된 상품평들을 조회해오기 위한 메서드.
     *
     * @param pageable  페이지 정보
     * @param productNo 상품 번호
     * @return 상품평 정보가 담긴 Dto Page 정보
     */
    Page<GetProductReviewResponseDto> findProductReviews(Pageable pageable, Long productNo);

    /**
     * 해당 회원이 작성한 상품평들을 조회해오기 위한 메서드.
     *
     * @param pageable 페이지 정보
     * @param memberNo 회원 번호
     * @return 해당 회원이 작성한 상품평 정보가 담긴 Dto Page 정보
     */
    Page<GetMemberReviewResponseDto> findMemberReviews(Pageable pageable, Long memberNo);

    /**
     * 해당 회원이 상품평 작성 가능한 상품들을 조회해오기 위한 메서드.
     *
     * @param pageable 페이지 정보
     * @param memberNo 회원 번호
     * @return 해당 회원이 상품평 작성 가능한 상품들의 간단한 정보가 담긴 Dto Page 정보
     */
    Page<GetProductSimpleResponseDto> findWritableMemberReviews(Pageable pageable, Long memberNo);

    /**
     * 상품평 단건 조회를 위한 메서드.
     *
     * @param reviewNo 상품평 번호
     * @return 상품평 정보가 담긴 Dto
     */
    Optional<GetMemberReviewResponseDto> findReview(Long reviewNo);

    /**
     * 해당 상품의 상품평 정보를 조회하기 위한 메서드.
     *
     * @param productNo 상품 번호
     * @return 해당 상품의 간단한 상품평 정보가 담긴 Dto
     */
    Optional<GetProductReviewInfoResponseDto> findReviewInfoByProductNo(Long productNo);

    /**
     * 리뷰가 삭제된 상품인지 확인하는 메서드입니다.
     *
     * @param memberNo  회원번호
     * @param productNo 상품번호
     * @return the boolean
     */
    boolean checkDeletedReview(Long memberNo, Long productNo);
}
