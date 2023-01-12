package com.nhnacademy.bookpubshop.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
    Product product;
    ReviewPolicy reviewPolicy;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(product);
        entityManager.persist(reviewPolicy);
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
        assertThat(findReview.get().getImagePath()).isEqualTo(persist.getImagePath());
        assertThat(findReview.get().getCreatedAt()).isAfter(now);
    }
}