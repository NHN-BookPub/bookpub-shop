package com.nhnacademy.bookpubshop.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
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

    Tier tier;
    Member member;
    Review review;
    Product product;
    ReviewPolicy reviewPolicy;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        product = productDummy();
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);

        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(product);
        entityManager.persist(reviewPolicy);
    }

    @Test
    @DisplayName("상품평 저장 테스트")
    void reviewSaveTest() {
        entityManager.persist(review);
        entityManager.clear();

        Optional<Review> findReview = reviewRepository.findById(1L);

        assertThat(findReview).isPresent();
        assertThat(findReview.get().getReviewStar()).isEqualTo(5L);
        assertThat(findReview.get().getProduct().getProductIsbn()).isEqualTo("isbn");
        assertThat(findReview.get().getProduct().getProductSaleStateCode().getCodeInfo())
                .isEqualTo("info");
    }

    private Product productDummy() {
        Product product = new Product(null, productPolicyDummy(), productTypeStateCodeDummy(),
                productSaleStateCodeDummy(), "isbn",
                "title", 10, "description",
                "test", "file_path", 10L, 1,
                1L, 1, false,
                1, LocalDateTime.now(), LocalDateTime.now(), false);
        return entityManager.persist(product);
    }

    private ProductTypeStateCode productTypeStateCodeDummy() {
        return entityManager.persist(new ProductTypeStateCode(null, "code",
                true, "info"));
    }

    private ProductPolicy productPolicyDummy() {
        return entityManager.persist(new ProductPolicy(null, "test_policy",
                false, 1));
    }

    private ProductSaleStateCode productSaleStateCodeDummy() {
        return entityManager.persist(new ProductSaleStateCode(null, "category",
                true, "info"));
    }
}