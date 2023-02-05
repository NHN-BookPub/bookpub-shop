package com.nhnacademy.bookpubshop.review.service;

import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품평 서비스를 위한 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
public interface ReviewService {

    /**
     * 특정 상품의 상품평 리스트를 페이지 형식으로 조회하는 메서드입니다.
     *
     * @param pageable 페이지 정보
     * @return 상품평 정보를 담은 Dto 페이지 정보
     */
    Page<GetProductReviewResponseDto> getProductReviews(Pageable pageable, Long productNo);

    /**
     * 상품평 단건 조회를 위한 메서드입니다.
     *
     * @param reviewNo 조회할 상품평 번호
     * @return 상품평 정보를 담은 Dto
     */
    GetMemberReviewResponseDto getReview(Long reviewNo);

    /**
     * 특정 회원이 작성한 상품평 리스트를 페이지 형식으로 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이지 정보
     * @return 상품평 정보를 담은 Dto 페이지 정보
     */
    Page<GetMemberReviewResponseDto> getMemberReviews(Pageable pageable, Long memberNo);

    /**
     * 특정 회원이 상품평 작성 가능한 상품 리스트를 페이지 형식으로 조회하기 위한 메서드입니다.
     *
     * @param pageable 페이지 정보
     * @param member   회원 번호
     * @return 상품평 작성 가능한 상품 정보가 담긴 Dto Page 정보
     */
    Page<GetProductSimpleResponseDto> getWritableMemberReviews(Pageable pageable, Long member);

    /**
     * 특정 상품에 관한 간단한 상품평 정보를 조회하기 위한 메서드입니다.
     *
     * @param productNo 상품 번호
     * @return 해당 상품에 관한 상품평 정보를 담은 dto
     */
    GetProductReviewInfoResponseDto getReviewInfo(Long productNo);

    /**
     * 상품평을 등록하기 위한 메서드입니다.
     *
     * @param createRequestDto 상품평 등록에 필요한 정보를 담은 Dto
     */
    void createReview(CreateReviewRequestDto createRequestDto, MultipartFile image);

    /**
     * 상품평을 수정하기 위한 메서드입니다.
     *
     * @param modifyRequestDto 상품평 수정에 필요한 정보를 담은 Dto
     */
    void modifyReview(Long reviewNo, ModifyReviewRequestDto modifyRequestDto, MultipartFile image);

    /**
     * 상품평 수정 시 이미지 삭제를 위한 메서드입니다.
     *
     * @param reviewNo 상품평 번호
     */
    void deleteReviewImage(Long reviewNo);

    /**
     * 상품평을 삭제하기 위한 메서드입니다.
     *
     * @param reviewNo 상품평 번호
     */
    void deleteReview(Long reviewNo);
}
