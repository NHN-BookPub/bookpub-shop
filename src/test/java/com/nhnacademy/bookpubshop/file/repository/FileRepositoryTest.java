package com.nhnacademy.bookpubshop.file.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.service.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
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
 * FileRepoTest
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    File file;

    Member member;

    @BeforeEach
    void setUp() {
        member = memberDummy();
        file = FileDummy.dummy(PersonalInquiryDummy.dummy(member), couponDummy(member), productDummy(),
                CustomerServiceDummy.dummy(CustomerServiceStateCodeDummy.dummy(),member));
    }

    @DisplayName("파일 Repo save 테스트")
    @Test
    void fileRepositorySaveTest() {
        entityManager.persist(file.getCustomerService().getCustomerServiceStateCode());
        entityManager.persist(file.getCustomerService());
        File persist = entityManager.persist(file);

        Optional<File> result = fileRepository.findById(persist.getFileNo());
        assertThat(result).isPresent();
        assertThat(result.get().getFileNo()).isEqualTo(persist.getFileNo());
        assertThat(result.get().getFileCategory()).isEqualTo(persist.getFileCategory());
        assertThat(result.get().getCreatedAt()).isEqualTo(persist.getCreatedAt());
        assertThat(result.get().getFilePath()).isEqualTo(persist.getFilePath());
        assertThat(result.get().getFileExtension()).isEqualTo(persist.getFileExtension());
        assertThat(result.get().getNameSaved()).isEqualTo(persist.getNameSaved());
        assertThat(result.get().getPersonalInquiry().getPersonalInquiryNo())
                .isEqualTo(persist.getPersonalInquiry().getPersonalInquiryNo());
        assertThat(result.get().getCustomerService().getServiceNo())
                .isEqualTo(persist.getCustomerService().getServiceNo());
        assertThat(result.get().getCoupon().getCouponNo())
                .isEqualTo(persist.getCoupon().getCouponNo());
        assertThat(result.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(result.get().getFileNo()).isEqualTo(persist.getFileNo());
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

    private Member memberDummy() {
        Member testMember = new Member(null, new Tier(null, "tier"),
                "test_id", "test_nickname", "test_name", "남",
                22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false,
                null, 0L, false);
        entityManager.persist(testMember.getTier());
        return entityManager.persist(testMember);
    }

    private Coupon couponDummy(Member member) {
        Coupon coupon = new Coupon(null, member, false, LocalDateTime.now());
        return entityManager.persist(coupon);
    }
}