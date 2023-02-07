package com.nhnacademy.bookpubshop.review.controller;

import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.service.ReviewService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품평을 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 상품에 따른 상품평 리스트를 조회하기 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param pageable 페이지 정보
     * @return 상품평 정보를 담은 Dto 페이지 정보
     */
    @GetMapping("/api/reviews/product/{productNo}")
    public ResponseEntity<PageResponse<GetProductReviewResponseDto>> productReviewList(
            Pageable pageable, @PathVariable Long productNo) {
        Page<GetProductReviewResponseDto> reviews = reviewService.getProductReviews(pageable, productNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(reviews));
    }

    /**
     * 상품평 단건 조회를 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param reviewNo 조회할 상품평 번호
     * @return 상품평 정보를 담은 Dto
     */
    @GetMapping("/api/reviews/{reviewNo}")
    public ResponseEntity<GetMemberReviewResponseDto> reviewDetails(@PathVariable Long reviewNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.getReview(reviewNo));
    }

    /**
     * 회원이 작성한 상품평 조회를 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param pageable 페이지 정보
     * @return 상품평 정보를 담은 Dto 페이지 정보
     */
    @MemberAuth
    @GetMapping("/token/reviews/member/{memberNo}")
    public ResponseEntity<PageResponse<GetMemberReviewResponseDto>> memberReviewList(
            Pageable pageable, @PathVariable Long memberNo) {

        Page<GetMemberReviewResponseDto> reviews = reviewService.getMemberReviews(pageable, memberNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(reviews));
    }

    /**
     * 회원이 리뷰를 작성할 수 있는 상품정보들을 조회하기 위한 메서드입니다.
     * 성공 시 200 반환.
     *
     * @param pageable 페이지 정보
     * @param memberNo 회원 번호
     * @return 상품정보들이 담긴 Dto 페이지 정보
     */
    @MemberAuth
    @GetMapping("/token/reviews/member/{memberNo}/writable")
    public ResponseEntity<PageResponse<GetProductSimpleResponseDto>> memberWritableReviewList(
            Pageable pageable, @PathVariable Long memberNo) {
        Page<GetProductSimpleResponseDto> reviews =
                reviewService.getWritableMemberReviews(pageable, memberNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(reviews));
    }

    /**
     * 상품에 대한 리뷰 정보들을 조회하기 위한 메서드입니다.
     * 상품의 리뷰평점, 리뷰갯수 등을 조회할 수 있습니다.
     * 성공 시 200 반환.
     *
     * @param productNo 상품 번호
     * @return 상품에 대한 리뷰 정보들이 담긴 Dto
     */
    @GetMapping("/api/reviews/info/product/{productNo}")
    public ResponseEntity<GetProductReviewInfoResponseDto> reviewInfoProduct(
            @PathVariable("productNo") Long productNo) {
        GetProductReviewInfoResponseDto reviewInfo = reviewService.getReviewInfo(productNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewInfo);
    }

    /**
     * 상품평 등록을 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param createRequestDto 상품평 등록에 필요한 정보를 담은 Dto
     * @return the response entity
     */
    @MemberAuth
    @PostMapping("/token/reviews/members/{memberNo}")
    public ResponseEntity<Void> reviewAdd(@Valid @RequestPart("createRequestDto")
                                          CreateReviewRequestDto createRequestDto,
                                          @RequestPart(value = "image", required = false)
                                          MultipartFile image) {
        reviewService.createReview(createRequestDto, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품평 수정을 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param modifyRequestDto 상품평 수정에 필요한 정보를 담은 Dto.
     * @return the response entity
     */
    @MemberAuth
    @PutMapping("/token/reviews/{reviewNo}/content/members/{memberNo}")
    public ResponseEntity<Void> reviewModify(@PathVariable("reviewNo") Long reviewNo,
                                             @Valid @RequestPart("modifyRequestDto")
                                             ModifyReviewRequestDto modifyRequestDto,
                                             @RequestPart(value = "image", required = false) MultipartFile image, @PathVariable String memberNo) {
        reviewService.modifyReview(reviewNo, modifyRequestDto, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상품평 수정 시 이미지 삭제를 위한 메서드입니다.
     * 성공 시 201 반환.
     *
     * @param reviewNo 상품평 번호
     * @return the response entity
     */
    @MemberAuth
    @PutMapping("/token/reviews/{reviewNo}/file/members/{memberNo}")
    public ResponseEntity<Void> reviewDeleteFile(@PathVariable("reviewNo") Long reviewNo) {
        reviewService.deleteReviewImage(reviewNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품평 삭제를 위한 메서드입니다.
     * soft delete 를 위한 상태값 변경을 수행합니다.
     * 성공 시 201 반환.
     *
     * @param reviewNo 삭제할 상품평 번호
     * @return the response entity
     */
    @MemberAuth
    @PutMapping("/token/reviews/{reviewNo}/members/{memberNo}")
    public ResponseEntity<Void> reviewDelete(@PathVariable("reviewNo") Long reviewNo) {
        reviewService.deleteReview(reviewNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
