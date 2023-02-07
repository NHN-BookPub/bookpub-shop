package com.nhnacademy.bookpubshop.inquiry.repository;

import static com.nhnacademy.bookpubshop.state.InquiryState.EXCHANGE;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
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
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
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
    InquiryStateCodeRepository inquiryStateCodeRepository;

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
    InquiryStateCode inquiryStateCode;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        entityManager.persist(bookPubTier);
        entityManager.persist(member);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        inquiryStateCode = new InquiryStateCode(null, EXCHANGE.getName(), EXCHANGE.isUsed(), "교환");
        inquiryStateCodeRepository.save(inquiryStateCode);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
    }

    @Test
    @DisplayName(value = "상품문의(inquiry) 레포지토리 save 테스트")
    void inquirySaveTest() {
        LocalDateTime now = LocalDateTime.now();
        Inquiry inquiry = new Inquiry(null, null, member, product, inquiryStateCode, "content", false, false);
        inquiryRepository.save(inquiry);

        Optional<Inquiry> optional = inquiryRepository.findById(inquiry.getInquiryNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(optional.get().getInquiryContent()).isEqualTo(inquiry.getInquiryContent());
        assertThat(optional.get().isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(optional.get().getParentInquiry()).isNull();
        assertThat(optional.get().isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(optional.get().getInquiryStateCode().getInquiryCodeNo()).isEqualTo(inquiryStateCode.getInquiryCodeNo());
        assertThat(optional.get().getCreatedAt()).isAfter(now);

        entityManager.clear();
    }
}