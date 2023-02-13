package com.nhnacademy.bookpubshop.review.service.impl;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.point.repository.PointHistoryRepository;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.review.dto.request.CreateReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.request.ModifyReviewRequestDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookpubshop.review.repository.ReviewRepository;
import com.nhnacademy.bookpubshop.review.service.ReviewService;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.reviewpolicy.repository.ReviewPolicyRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품평 서비스 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(ReviewServiceImpl.class)
class ReviewServiceImplTest {

    @Autowired
    ReviewService reviewService;

    @MockBean
    ReviewRepository reviewRepository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    ReviewPolicyRepository reviewPolicyRepository;
    @MockBean
    FileManagement fileManagement;
    @MockBean
    PointHistoryRepository pointHistoryRepository;

    Member member;
    Product product;
    BookPubTier tier;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    File file;
    ReviewPolicy reviewPolicy;
    Review review;
    CreateReviewRequestDto createReviewDto;
    ModifyReviewRequestDto modifyReviewDto;
    ArgumentCaptor<Review> captor;

    @BeforeEach
    void setUp() {
        captor = ArgumentCaptor.forClass(Review.class);

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        file = FileDummy.dummy(null, null, null, null, null, null);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);

        createReviewDto = new CreateReviewRequestDto();
        ReflectionTestUtils.setField(createReviewDto, "memberNo", 1L);
        ReflectionTestUtils.setField(createReviewDto, "productNo", 1L);
        ReflectionTestUtils.setField(createReviewDto, "reviewStar", 5);
        ReflectionTestUtils.setField(createReviewDto, "reviewContent", "It's funny book");

        modifyReviewDto = new ModifyReviewRequestDto();
        ReflectionTestUtils.setField(modifyReviewDto, "reviewStar", 5);
        ReflectionTestUtils.setField(modifyReviewDto, "reviewContent", "It's sad book");
    }

    @Test
    @DisplayName("해당 상품의 상품평들 조회 성공 테스트")
    void getProductReviews() {
        // given
        GetProductReviewResponseDto getProductReviewDto =
                new GetProductReviewResponseDto(100L, "nickname", 5, "content", null, now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductReviewResponseDto> page = new PageImpl<>(List.of(getProductReviewDto), pageable, 1);

        // when
        when(reviewRepository.findProductReviews(any(), anyLong()))
                .thenReturn(page);

        // then
        Page<GetProductReviewResponseDto> result = reviewService.getProductReviews(pageable, 1L);
        List<GetProductReviewResponseDto> content = result.getContent();

        assertThat(content.get(0).getReviewNo()).isEqualTo(getProductReviewDto.getReviewNo());
        assertThat(content.get(0).getMemberNickname()).isEqualTo(getProductReviewDto.getMemberNickname());
        assertThat(content.get(0).getReviewStar()).isEqualTo(getProductReviewDto.getReviewStar());
        assertThat(content.get(0).getReviewContent()).isEqualTo(getProductReviewDto.getReviewContent());
        assertThat(content.get(0).getImagePath()).isEqualTo(getProductReviewDto.getImagePath());
        assertThat(content.get(0).getCreatedAt()).isEqualTo(getProductReviewDto.getCreatedAt());

        verify(reviewRepository, times(1)).findProductReviews(any(), anyLong());

    }

    @Test
    @DisplayName("리뷰 단건 조회 성공 테스트")
    void getReview() {
        //given
        GetMemberReviewResponseDto getMemberReviewDto =
                new GetMemberReviewResponseDto(1L, 1L, "title",
                        "publisher", List.of("name"), "image1",
                        5, "content", "image2", now());

        //when
        when(reviewRepository.findReview(anyLong())).thenReturn(Optional.of(getMemberReviewDto));

        //then
        GetMemberReviewResponseDto result = reviewService.getReview(getMemberReviewDto.getReviewNo());

        assertThat(result.getReviewNo()).isEqualTo(getMemberReviewDto.getReviewNo());
        assertThat(result.getProductNo()).isEqualTo(getMemberReviewDto.getProductNo());
        assertThat(result.getProductTitle()).isEqualTo(getMemberReviewDto.getProductTitle());
        assertThat(result.getProductPublisher()).isEqualTo(getMemberReviewDto.getProductPublisher());
        assertThat(result.getProductAuthorNames()).isEqualTo(getMemberReviewDto.getProductAuthorNames());
        assertThat(result.getProductImagePath()).isEqualTo(getMemberReviewDto.getProductImagePath());
        assertThat(result.getReviewStar()).isEqualTo(getMemberReviewDto.getReviewStar());
        assertThat(result.getReviewContent()).isEqualTo(getMemberReviewDto.getReviewContent());
        assertThat(result.getReviewImagePath()).isEqualTo(getMemberReviewDto.getReviewImagePath());
        assertThat(result.getCreatedAt()).isEqualTo(getMemberReviewDto.getCreatedAt());

        verify(reviewRepository, times(1)).findReview(anyLong());
    }

    @Test
    @DisplayName("리뷰 단건 조회 실패 테스트")
    void getReviewTest_Fail_ReviewNotFound() {
        //when
        when(reviewRepository.findReview(anyLong())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> reviewService.getReview(1L))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(ReviewNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findReview(anyLong());

    }

    @Test
    @DisplayName("해당 회원의 상품평 조회 성공 테스트")
    void getMemberReviews() {
        //given
        GetMemberReviewResponseDto getMemberReviewDto =
                new GetMemberReviewResponseDto(1L, 1L, "title",
                        "publisher", List.of("name"), "image1",
                        5, "content", "image2", now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetMemberReviewResponseDto> page = new PageImpl<>(List.of(getMemberReviewDto), pageable, 1);

        //when
        when(reviewRepository.findMemberReviews(any(), anyLong())).thenReturn(page);

        //then
        Page<GetMemberReviewResponseDto> result = reviewService.getMemberReviews(pageable, 1L);
        List<GetMemberReviewResponseDto> content = result.getContent();

        assertThat(content.get(0).getReviewNo()).isEqualTo(getMemberReviewDto.getReviewNo());
        assertThat(content.get(0).getProductNo()).isEqualTo(getMemberReviewDto.getProductNo());
        assertThat(content.get(0).getProductTitle()).isEqualTo(getMemberReviewDto.getProductTitle());
        assertThat(content.get(0).getProductPublisher()).isEqualTo(getMemberReviewDto.getProductPublisher());
        assertThat(content.get(0).getProductAuthorNames()).isEqualTo(getMemberReviewDto.getProductAuthorNames());
        assertThat(content.get(0).getProductImagePath()).isEqualTo(getMemberReviewDto.getProductImagePath());
        assertThat(content.get(0).getReviewStar()).isEqualTo(getMemberReviewDto.getReviewStar());
        assertThat(content.get(0).getReviewContent()).isEqualTo(getMemberReviewDto.getReviewContent());
        assertThat(content.get(0).getReviewImagePath()).isEqualTo(getMemberReviewDto.getReviewImagePath());
        assertThat(content.get(0).getCreatedAt()).isEqualTo(getMemberReviewDto.getCreatedAt());

        verify(reviewRepository, times(1)).findMemberReviews(any(), anyLong());
    }

    @Test
    @DisplayName("해당 회원의 상품평 작성 가능한 상품들 정보 조회 성공")
    void getWritableMemberReviews() {
        // given
        GetProductSimpleResponseDto getProductSimpleDto =
                new GetProductSimpleResponseDto(1L, "title", "isbn", "publisher", List.of("authorName"), "image");

        Pageable pageable = PageRequest.of(0, 10);
        Page<GetProductSimpleResponseDto> page = new PageImpl<>(List.of(getProductSimpleDto), pageable, 1);

        // when
        when(reviewRepository.findWritableMemberReviews(any(), anyLong())).thenReturn(page);

        // then
        Page<GetProductSimpleResponseDto> result = reviewService.getWritableMemberReviews(pageable, 1L);
        List<GetProductSimpleResponseDto> content = result.getContent();

        assertThat(content.get(0).getProductNo()).isEqualTo(getProductSimpleDto.getProductNo());
        assertThat(content.get(0).getTitle()).isEqualTo(getProductSimpleDto.getTitle());
        assertThat(content.get(0).getProductIsbn()).isEqualTo(getProductSimpleDto.getProductIsbn());
        assertThat(content.get(0).getProductPublisher()).isEqualTo(getProductSimpleDto.getProductPublisher());
        assertThat(content.get(0).getProductAuthorNames()).isEqualTo(getProductSimpleDto.getProductAuthorNames());
        assertThat(content.get(0).getProductImagePath()).isEqualTo(getProductSimpleDto.getProductImagePath());

        verify(reviewRepository, times(1)).findWritableMemberReviews(any(), anyLong());
    }

    @Test
    @DisplayName("해당 상품의 상품평 요약정보 조회 성공")
    void getReviewInfo() {
        //given
        GetProductReviewInfoResponseDto dto =
                new GetProductReviewInfoResponseDto(30L, 4);

        //when
        when(reviewRepository.findReviewInfoByProductNo(anyLong())).thenReturn(Optional.of(dto));

        //then
        GetProductReviewInfoResponseDto result = reviewService.getReviewInfo(1L);

        assertThat(result.getReviewCount()).isEqualTo(dto.getReviewCount());
        assertThat(result.getProductStar()).isEqualTo(dto.getProductStar());

        verify(reviewRepository, times(1)).findReviewInfoByProductNo(anyLong());
    }

    @Test
    @DisplayName("해당 상품의 상품평 요약정보 조회 실패_상품이 없을때")
    void getReviewInfoTest_Fail_ProductNotFound() {
        //when
        when(reviewRepository.findReviewInfoByProductNo(anyLong())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> reviewService.getReviewInfo(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findReviewInfoByProductNo(anyLong());
    }

    @Test
    @DisplayName("상품평 등록 성공 테스트")
    void createReview() throws IOException {
        //given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenReturn(file);
        when(reviewPolicyRepository.findByPolicyUsedIsTrue()).thenReturn(Optional.of(reviewPolicy));
        when(reviewRepository.save(any())).thenReturn(review);

        //then
        reviewService.createReview(createReviewDto, multipartFile);
        verify(reviewRepository, times(1))
                .save(captor.capture());
        Review result = captor.getValue();

        assertThat(result.getReviewStar()).isEqualTo(createReviewDto.getReviewStar());
        assertThat(result.getReviewContent()).isEqualTo(createReviewDto.getReviewContent());

        verify(productRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(fileManagement).saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());
    }

    @Test
    @DisplayName("상품평 등록 실패 테스트_파일 문제 에러 발생")
    void createReview_Fail_FileNotFound() throws IOException {
        //given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(reviewPolicyRepository.findByPolicyUsedIsTrue()).thenReturn(Optional.of(reviewPolicy));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);
        when(reviewRepository.save(any())).thenReturn(review);

        //then
        assertThatThrownBy(() -> reviewService.createReview(createReviewDto, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품평 수정 성공 테스트")
    void modifyReview() throws IOException {
        //given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenReturn(file);

        //then
        reviewService.modifyReview(1L, modifyReviewDto, multipartFile);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(1)).saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());

    }

    @Test
    @DisplayName("상품평 수정 실패 테스트_상품평이 없는 에러 발생")
    void modifyReview_Fail_ReviewNotFound() throws IOException {
        //given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);
        when(reviewRepository.save(any())).thenReturn(review);

        //then
        assertThatThrownBy(() -> reviewService.modifyReview(1L, modifyReviewDto, multipartFile))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(ReviewNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품평 수정 실패 테스트_파일 문제 에러 발생")
    void modifyReview_Fail_FileNotFound() throws IOException {
        //given
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenThrow(IOException.class);
        when(reviewRepository.save(any())).thenReturn(review);

        //then
        assertThatThrownBy(() -> reviewService.modifyReview(1L, modifyReviewDto, multipartFile))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("상품평의 이미지 삭제 성공 테스트")
    void deleteReviewImage() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        doNothing().when(fileManagement).deleteFile(anyString());

        //then
        reviewService.deleteReviewImage(1L);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(1)).deleteFile(anyString());
    }

    @Test
    @DisplayName("상품평의 이미지 실패 성공 테스트_상품평이 없을 경우")
    void deleteReviewImage_Fail_ReviewNotFound() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(fileManagement).deleteFile(any());

        //then
        assertThatThrownBy(() -> reviewService.deleteReviewImage(1L))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(ReviewNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(0)).deleteFile(any());
    }

    @Test
    @DisplayName("상품평의 이미지 삭제 실패 테스트_파일 에러 뜬 경우")
    void deleteReviewImage_Fail_FileNotFound() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        doThrow(new IOException()).when(fileManagement).deleteFile(anyString());

        //then
        assertThatThrownBy(() -> reviewService.deleteReviewImage(1L))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("상품평 삭제 성공 테스트")
    void deleteReview() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        doNothing().when(fileManagement).deleteFile(anyString());

        //then
        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(1)).deleteFile(anyString());
    }

    @Test
    @DisplayName("상품평 삭제 실패 테스트_상품평이 없을 경우")
    void deleteReview_Fail_ReviewNotFound() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(fileManagement).deleteFile(anyString());

        //then
        assertThatThrownBy(() -> reviewService.deleteReview(1L))
                .isInstanceOf(ReviewNotFoundException.class)
                .hasMessageContaining(ReviewNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(0)).deleteFile(anyString());
    }

    @Test
    @DisplayName("상품평 삭제 실패 테스트_파일 입출력 에러 난 경우")
    void deleteReview_Fail_FileNotFound() throws IOException {
        //given
        review.setFile(file);

        //when
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        doThrow(new IOException()).when(fileManagement).deleteFile(anyString());

        //then
        assertThatThrownBy(() -> reviewService.deleteReview(1L))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining(FileNotFoundException.MESSAGE);

        verify(reviewRepository, times(1)).findById(anyLong());
        verify(fileManagement, times(1)).deleteFile(anyString());
    }
}