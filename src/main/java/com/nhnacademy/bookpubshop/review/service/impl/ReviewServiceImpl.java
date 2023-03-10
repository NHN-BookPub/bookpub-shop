package com.nhnacademy.bookpubshop.review.service.impl;

import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.point.entity.PointHistory;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookpubshop.review.repository.ReviewRepository;
import com.nhnacademy.bookpubshop.review.service.ReviewService;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.exception.ReviewPolicyUsedNotFoundException;
import com.nhnacademy.bookpubshop.reviewpolicy.repository.ReviewPolicyRepository;
import com.nhnacademy.bookpubshop.state.FileCategory;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * ????????? ????????? ??????????????????.
 *
 * @author : ?????????
 * @since : 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private static final String REVIEW = "?????? ??????";
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewPolicyRepository reviewPolicyRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final FileManagement fileManagement;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductReviewResponseDto> getProductReviews(Pageable pageable, Long productNo) {
        return reviewRepository.findProductReviews(pageable, productNo);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException ???????????? ?????? ??? ???????????? exception
     */
    @Override
    public GetMemberReviewResponseDto getReview(Long reviewNo) {
        return reviewRepository.findReview(reviewNo).orElseThrow(
                () -> new ReviewNotFoundException(reviewNo)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetMemberReviewResponseDto> getMemberReviews(Pageable pageable, Long memberNo) {
        return reviewRepository.findMemberReviews(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductSimpleResponseDto> getWritableMemberReviews(
            Pageable pageable, Long memberNo) {
        return reviewRepository.findWritableMemberReviews(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ProductNotFoundException ????????? ?????? ??? ???????????? exception
     */
    @Override
    public GetProductReviewInfoResponseDto getReviewInfo(Long productNo) {
        return reviewRepository.findReviewInfoByProductNo(productNo).orElseThrow(
                ProductNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ProductNotFoundException ????????? ?????? ??? ???????????? exception
     * @throws MemberNotFoundException  ????????? ?????? ??? ???????????? exception
     * @throws FileNotFoundException    ????????? ????????? ?????? ??? ???????????? Exception
     * @thorws ReviewPolicyUsedNotFoundException ???????????? ????????? ????????? ?????? ??? ???????????? exception
     */
    @Transactional
    @Override
    public void createReview(CreateReviewRequestDto createRequestDto, MultipartFile image) {
        Product product = productRepository.findById(createRequestDto.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        Member member = memberRepository.findById(createRequestDto.getMemberNo())
                .orElseThrow(MemberNotFoundException::new);

        ReviewPolicy reviewPolicy = reviewPolicyRepository.findByPolicyUsedIsTrue()
                .orElseThrow(ReviewPolicyUsedNotFoundException::new);

        Review review = reviewRepository.save(Review.builder()
                .member(member)
                .product(product)
                .reviewPolicy(reviewPolicy)
                .reviewStar(createRequestDto.getReviewStar())
                .reviewContent(createRequestDto.getReviewContent())
                .build());

        checkIsDeletedReview(product, member, reviewPolicy);


        try {
            review.setFile(fileManagement.saveFile(null, null, null,
                    review, null, null, image,
                    FileCategory.REVIEW.getCategory(), FileCategory.REVIEW.getPath()));
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * ????????? ?????? ????????? ?????????????????? ???????????? ????????? ?????????.
     *
     * @param product ????????????
     * @param member ????????????
     * @param reviewPolicy ?????? ??????
     */
    private void checkIsDeletedReview(Product product, Member member, ReviewPolicy reviewPolicy) {
        if (reviewRepository.checkDeletedReview(member.getMemberNo(), product.getProductNo())) {
            pointHistoryRepository.save(PointHistory.builder()
                    .member(member)
                    .pointHistoryIncreased(true)
                    .pointHistoryReason(REVIEW)
                    .pointHistoryAmount(reviewPolicy.getSendPoint())
                    .build());
            member.increaseMemberPoint(reviewPolicy.getSendPoint());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException ???????????? ?????? ??? ???????????? Exception
     * @throws FileNotFoundException   ????????? ????????? ?????? ??? ???????????? Exception
     */
    @Override
    @Transactional
    public void modifyReview(Long reviewNo, ModifyReviewRequestDto modifyRequestDto,
                             MultipartFile image) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new ReviewNotFoundException(reviewNo));

        review.modifyReview(modifyRequestDto);

        if (Objects.nonNull(image)) {
            try {
                review.setFile(fileManagement.saveFile(null, null, null,
                        review, null, null, image, "review", "review"));

            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException ???????????? ?????? ??? ???????????? Exception
     * @throws FileNotFoundException   ????????? ????????? ?????? ??? ???????????? Exception
     */
    @Override
    @Transactional
    public void deleteReviewImage(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new ReviewNotFoundException(reviewNo));

        if (Objects.nonNull(review.getFile())) {
            try {
                fileManagement.deleteFile(review.getFile().getFilePath());
            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }

        review.deleteFile();
    }

    /**
     * {@inheritDoc}
     *
     * @throws ReviewNotFoundException ???????????? ?????? ??? ???????????? Exception
     * @throws FileNotFoundException   ????????? ?????? ??? ???????????? Exception
     */
    @Override
    @Transactional
    public void deleteReview(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new ReviewNotFoundException(reviewNo));

        review.deleteReview();

        if (Objects.nonNull(review.getFile())) {
            try {
                fileManagement.deleteFile(review.getFile().getFilePath());
            } catch (IOException e) {
                throw new FileNotFoundException();
            }
        }
    }
}
