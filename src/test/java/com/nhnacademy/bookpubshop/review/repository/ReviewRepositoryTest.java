package com.nhnacademy.bookpubshop.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.author.dummy.AuthorDummy;
import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.dummy.OrderProductDummy;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductSimpleResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.review.dto.response.GetMemberReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewInfoResponseDto;
import com.nhnacademy.bookpubshop.review.dto.response.GetProductReviewResponseDto;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 상품평 레포지토리 테스트
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TestEntityManager entityManager;

    BookPubTier bookPubTier;
    Member member;
    Review review;
    Author author;
    Product product;
    ProductAuthor productAuthor;
    ReviewPolicy reviewPolicy;
    ProductPolicy productPolicy;
    PricePolicy pricePolicy;
    File file;
    File productFile;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        reviewPolicy = ReviewPolicyDummy.dummy();
        pricePolicy = PricePolicyDummy.dummy();

        author = AuthorDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        productAuthor = new ProductAuthor(new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()),
                author, product);
        review = ReviewDummy.dummy(member, product, reviewPolicy);
        file = FileDummy.dummy(null, review, null, null, null, null);
        productFile = FileDummy.dummy(null, null, null, product, null, null);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(author);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(reviewPolicy);
        entityManager.persist(product);
        entityManager.persist(productAuthor);
        entityManager.persist(file);
        entityManager.persist(productFile);
        entityManager.persist(pricePolicy);

        product.getProductAuthors().add(productAuthor);
        review.setFile(file);
        product.setProductFiles(List.of(productFile));

    }

    @Test
    @DisplayName("상품평 저장 테스트")
    void reviewSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Review persist = entityManager.persist(review);

        Optional<Review> findReview = reviewRepository.findById(persist.getReviewNo());

        assertThat(findReview).isPresent();
        assertThat(findReview.get().getMember().getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(findReview.get().getReviewStar()).isEqualTo(persist.getReviewStar());
        assertThat(findReview.get().getProduct().getProductIsbn()).isEqualTo(persist.getProduct().getProductIsbn());
        assertThat(findReview.get().getProduct().getProductSaleStateCode().getCodeInfo()).isEqualTo(persist.getProduct().getProductSaleStateCode().getCodeInfo());
        assertThat(findReview.get().getReviewPolicy().getPolicyNo()).isEqualTo(reviewPolicy.getPolicyNo());
        assertThat(findReview.get().getReviewContent()).isEqualTo(persist.getReviewContent());
        assertThat(findReview.get().getCreatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("상품에 관한 상품평 조회 테스트")
    void findProductReviewsTest() {
        Pageable pageable = Pageable.ofSize(10);

        entityManager.persist(review);

        Page<GetProductReviewResponseDto> result = reviewRepository.findProductReviews(pageable, review.getProduct().getProductNo());
        List<GetProductReviewResponseDto> content = result.getContent();

        assertThat(result).isNotEmpty();
        assertThat(content.get(0).getReviewNo()).isEqualTo(review.getReviewNo());
        assertThat(content.get(0).getMemberNickname()).isEqualTo(review.getMember().getMemberNickname());
        assertThat(content.get(0).getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(content.get(0).getReviewStar()).isEqualTo(review.getReviewStar());
        assertThat(content.get(0).getImagePath()).isEqualTo(review.getFile().getFilePath());
        assertThat(content.get(0).getCreatedAt()).isEqualTo(review.getCreatedAt());
    }

    @Test
    @DisplayName("해당 회원이 작성한 상품평 조회 테스트")
    void findMemberReviewsTest() {
        // given
        Product reviewProduct = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        File reviewProductFile = new File(null, null, null, null, null, reviewProduct, null,
                FileCategory.PRODUCT_THUMBNAIL.getCategory(), "d", "d", "d", "d");
        Review memberReview = ReviewDummy.dummy(member, reviewProduct, reviewPolicy);
        File reviewFile = FileDummy.dummy(null, memberReview, null, null, null, null);
        ProductAuthor reviewProductAuthor = new ProductAuthor(new ProductAuthor.Pk(author.getAuthorNo(), reviewProduct.getProductNo()),
                author, reviewProduct);

        entityManager.persist(review);
//        entityManager.persist(reviewProduct.getRelationProduct().get(0));
        entityManager.persist(reviewProduct);
        entityManager.persist(reviewProductFile);
        entityManager.persist(reviewProductAuthor);
        entityManager.persist(reviewFile);
        entityManager.persist(memberReview);

        reviewProduct.getProductAuthors().add(reviewProductAuthor);
        reviewProduct.setProductFiles(List.of(reviewProductFile));
        memberReview.setFile(reviewFile);

        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetMemberReviewResponseDto> result = reviewRepository.findMemberReviews(pageable, review.getMember().getMemberNo());
        List<GetMemberReviewResponseDto> content = result.getContent();

        // then
        assertThat(result).isNotEmpty();
        assertThat(content.get(0).getReviewNo()).isEqualTo(memberReview.getReviewNo());
        assertThat(content.get(0).getProductNo()).isEqualTo(memberReview.getProduct().getProductNo());
        assertThat(content.get(0).getProductTitle()).isEqualTo(memberReview.getProduct().getTitle());
        assertThat(content.get(0).getProductPublisher()).isEqualTo(memberReview.getProduct().getProductPublisher());
        assertThat(memberReview.getProduct().getProductAuthors()
                .stream().anyMatch(a -> a.getAuthor().getAuthorName()
                        .equals(content.get(0).getProductAuthorNames().get(0))))
                .isTrue();
        assertThat(content.get(0).getReviewStar()).isEqualTo(memberReview.getReviewStar());
        assertThat(content.get(0).getReviewContent()).isEqualTo(memberReview.getReviewContent());
        assertThat(content.get(0).getReviewImagePath()).isEqualTo(memberReview.getFile().getFilePath());
        assertThat(content.get(0).getCreatedAt()).isEqualTo(memberReview.getCreatedAt());
    }

    @Test
    @DisplayName("해당 회원이 상품평 작성 가능한 상품들 조회 테스트")
    void findWritableMemberReviewsTest() {
        // given
        Product reviewProduct = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        File reviewProductFile = new File(null, null, null, null, null, reviewProduct, null,
                FileCategory.PRODUCT_THUMBNAIL.getCategory(), "d", "d", "d", "d");
        OrderStateCode reviewOrderStateCode = new OrderStateCode(
                null, OrderState.COMPLETE_DELIVERY.getName(), OrderState.COMPLETE_DELIVERY.isUsed(), null);
        BookpubOrder reviewOrder = OrderDummy.dummy(review.getMember(), pricePolicy, pricePolicy, reviewOrderStateCode);
        OrderProductStateCode reviewOrderProductStateCode = new OrderProductStateCode(
                null, OrderProductState.CONFIRMED.getName(), OrderProductState.CONFIRMED.isUsed(), null);
        OrderProduct reviewOrderProduct = OrderProductDummy.dummy(reviewProduct, reviewOrder, reviewOrderProductStateCode);
        ProductAuthor reviewProductAuthor = new ProductAuthor(new ProductAuthor.Pk(author.getAuthorNo(), reviewProduct.getProductNo()),
                author, reviewProduct);

//        entityManager.persist(reviewProduct.getRelationProduct().get(0));
        entityManager.persist(reviewProduct);
        entityManager.persist(reviewProductFile);
        entityManager.persist(reviewOrderStateCode);
        entityManager.persist(reviewOrder);
        entityManager.persist(reviewOrderProductStateCode);
        entityManager.persist(reviewOrderProduct);
        entityManager.persist(reviewProductAuthor);
        entityManager.persist(review);

        reviewProduct.getProductAuthors().add(reviewProductAuthor);
        reviewProduct.setProductFiles(List.of(reviewProductFile));

        Pageable pageable = Pageable.ofSize(10);

        // when
        Page<GetProductSimpleResponseDto> result = reviewRepository.findWritableMemberReviews(pageable, reviewOrder.getMember().getMemberNo());
        List<GetProductSimpleResponseDto> content = result.getContent();

        // then
        assertThat(result).isNotEmpty();
        assertThat(content.get(0).getProductNo()).isEqualTo(reviewOrderProduct.getProduct().getProductNo());
        assertThat(content.get(0).getTitle()).isEqualTo(reviewOrderProduct.getProduct().getTitle());
        assertThat(content.get(0).getProductIsbn()).isEqualTo(reviewOrderProduct.getProduct().getProductIsbn());
        assertThat(content.get(0).getProductPublisher()).isEqualTo(reviewOrderProduct.getProduct().getProductPublisher());
        assertThat(reviewOrderProduct.getProduct().getProductAuthors()
                .stream().anyMatch(a -> a.getAuthor().getAuthorName()
                        .equals(content.get(0).getProductAuthorNames().get(0))))
                .isTrue();
        assertThat(content.get(0).getProductImagePath()).isEqualTo(reviewOrderProduct.getProduct().getFiles().get(0).getFilePath());
    }

    @Test
    @DisplayName("상품평 단건 조회 테스트")
    void findReviewTest() {
        File thumbnailFile = new File(null, null, null, null, null, product, null,
                FileCategory.PRODUCT_THUMBNAIL.getCategory(), "path", "extension", "origin", "saved");
        product.setProductFiles(List.of(thumbnailFile));
        entityManager.persist(review);

        Optional<GetMemberReviewResponseDto> result = reviewRepository.findReview(review.getReviewNo());

        assertThat(result).isPresent();
        assertThat(result.get().getReviewNo()).isEqualTo(review.getReviewNo());
        assertThat(result.get().getProductNo()).isEqualTo(review.getProduct().getProductNo());
        assertThat(result.get().getProductTitle()).isEqualTo(review.getProduct().getTitle());
        assertThat(result.get().getProductPublisher()).isEqualTo(review.getProduct().getProductPublisher());
        assertThat(review.getProduct().getProductAuthors()
                .stream().anyMatch(a -> a.getAuthor().getAuthorName()
                        .equals(result.get().getProductAuthorNames().get(0))))
                .isTrue();
        assertThat(result.get().getReviewStar()).isEqualTo(review.getReviewStar());
        assertThat(result.get().getReviewContent()).isEqualTo(review.getReviewContent());
        assertThat(result.get().getReviewImagePath()).isEqualTo(review.getFile().getFilePath());
        assertThat(result.get().getCreatedAt()).isEqualTo(review.getCreatedAt());
    }

    @Test
    @DisplayName("상품에 대한 상품평 요약정보 조회")
    void findReviewInfoByProductNo() {
        entityManager.persist(review);

        Optional<GetProductReviewInfoResponseDto> result = reviewRepository.findReviewInfoByProductNo(review.getProduct().getProductNo());

        assertThat(result).isPresent();
        assertThat(result.get().getProductStar()).isEqualTo(review.getReviewStar());
        assertThat(result.get().getReviewCount()).isEqualTo(1);

    }
}