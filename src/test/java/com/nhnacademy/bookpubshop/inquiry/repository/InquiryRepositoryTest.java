package com.nhnacademy.bookpubshop.inquiry.repository;

import static com.nhnacademy.bookpubshop.state.InquiryState.EXCHANGE;
import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirycode.entity.InquiryCode;
import com.nhnacademy.bookpubshop.inquirycode.repository.InquiryCodeRepository;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품문의(inquiry) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class InquiryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    InquiryCodeRepository inquiryCodeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Autowired
    InquiryRepository inquiryRepository;

    BookPubTier bookPubTier;
    Member member;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    InquiryCode inquiryCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        entityManager.persist(bookPubTier);
        entityManager.persist(member);

        productPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        productPolicyRepository.save(productPolicy);

        productTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        productTypeStateCodeRepository.save(productTypeStateCode);

        productSaleStateCode = new ProductSaleStateCode(null,
                SALE.getName(), SALE.isUsed(), "이 상품은 판매중입니다.");
        productSaleStateCodeRepository.save(productSaleStateCode);

        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode,
                Collections.EMPTY_LIST, "Isbn:123-1111", "title", "publisher", 100, "설명",
                "썸네일.png", "eBook path", 20000L,5000L,
                10, 1L, 3,  false, 100,
                LocalDateTime.now(), LocalDateTime.now(), false);
        productRepository.save(product);

        inquiryCode = new InquiryCode(null, EXCHANGE.getName(), EXCHANGE.isUsed(), "교환");
        inquiryCodeRepository.save(inquiryCode);
    }

    @Test
    @DisplayName(value = "상품문의(inquiry) 레포지토리 save 테스트")
    void inquirySaveTest() {
        LocalDateTime time = LocalDateTime.now();
        Inquiry inquiry = new Inquiry(null, null, member, product, inquiryCode, "content",
                false, time, false);
        inquiryRepository.save(inquiry);

        Optional<Inquiry> optional = inquiryRepository.findById(inquiry.getInquiryNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(optional.get().getInquiryContent()).isEqualTo(inquiry.getInquiryContent());
        assertThat(optional.get().isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(optional.get().getCreatedAt()).isEqualTo(inquiry.getCreatedAt());
        assertThat(optional.get().isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());

        entityManager.clear();
    }
}