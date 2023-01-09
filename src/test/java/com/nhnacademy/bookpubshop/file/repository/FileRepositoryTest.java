package com.nhnacademy.bookpubshop.file.repository;

import static com.nhnacademy.bookpubshop.state.CouponState.COUPON_ALL;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.customer_service.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.review.dummy.ReviewDummy;
import com.nhnacademy.bookpubshop.review.entity.Review;
import com.nhnacademy.bookpubshop.reviewpolicy.dummy.ReviewPolicyDummy;
import com.nhnacademy.bookpubshop.reviewpolicy.entity.ReviewPolicy;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
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

    Product product;

    BookpubOrder order;

    CouponTemplate couponTemplate;

    PricePolicy packagingPrice;

    PricePolicy deliveryPrice;

    Review review;

    ReviewPolicy reviewPolicy;
    @BeforeEach
    void setUp() {
        order = orderDummy();
        packagingPrice = PricePolicyDummy.dummy();
        deliveryPrice = PricePolicyDummy.dummy();

        product = productDummy();

        couponTemplate = CouponTemplateDummy.dummy(couponPolicyDummy(), couponType(), product, category(),
                couponStateCodeDummy());
        member = memberDummy();
        file = FileDummy.dummy(PersonalInquiryDummy.dummy(member),review, couponTemplate, product,
                CustomerServiceDummy.dummy(CustomerServiceStateCodeDummy.dummy(), member));
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);
    }

    @DisplayName("파일 Repo save 테스트")
    @Test
    void fileRepositorySaveTest() {
        entityManager.persist(review.getReviewPolicy());
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
        assertThat(result.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(result.get().getFileNo()).isEqualTo(persist.getFileNo());
    }

    private Product productDummy() {
        Product product = new Product(null, productPolicyDummy(), productTypeStateCodeDummy(),
                productSaleStateCodeDummy(), Collections.EMPTY_LIST, "isbn",
                "title", 10, "description",
                "test", "file_path", 10L, 100L,
                90, 1L, 0,
                false, 10, LocalDateTime.now(), LocalDateTime.now(), false);
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
        Member testMember = new Member(null, new BookPubTier(null, "tie2r"),
                "test_id", "test_nickname", "test_name", "남",
                22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false,
                null, 0L, false);
        entityManager.persist(testMember.getBookPubTier());
        return entityManager.persist(testMember);
    }

    private Address addressDummy() {
        return entityManager.persist(AddressDummy.dummy());
    }

    private BookpubOrder orderDummy() {
        BookPubTier bookPubTier = TierDummy.dummy();
        Member member2 = MemberDummy.dummy(bookPubTier);
        PricePolicy pricePolicy = PricePolicyDummy.dummy();
        PricePolicy packagePricePolicy = PricePolicyDummy.dummy();
        Address address = AddressDummy.dummy();
        OrderStateCode orderStateCode = OrderStateCodeDummy.dummy();

        entityManager.persist(bookPubTier);
        entityManager.persist(member2);
        entityManager.persist(pricePolicy);
        entityManager.persist(packagePricePolicy);
        entityManager.persist(address);
        entityManager.persist(orderStateCode);
        return entityManager.persist(OrderDummy.dummy(member2,
                pricePolicy, packagePricePolicy, address, orderStateCode));

    }

    private Coupon couponDummy(Member member) {
        Coupon coupon = new Coupon(null, couponTemplate, order, orderProductDummy(), member, false, LocalDateTime.now());
        entityManager.persist(coupon.getCouponTemplate());
        return entityManager.persist(coupon);
    }

    private OrderProductStateCode orderProductStateCodeDummy() {
        return entityManager.persist(new OrderProductStateCode(null, "code", true, "info"));
    }

    private OrderProduct orderProductDummy() {
        return entityManager.persist(new OrderProduct(null, product, order, orderProductStateCodeDummy(), 1, 1L, 1L, "info"));
    }

    private CouponPolicy couponPolicyDummy() {
        CouponPolicy couponPolicy =
                new CouponPolicy(null, false, 0L, 0L, 0L);
        return entityManager.persist(couponPolicy);
    }

    private CouponType couponType() {
        CouponType couponType = new CouponType(null, "coupon");
        return entityManager.persist(couponType);
    }

    private Category category() {
        Category category = new Category(null, null, "name",
                0, true);

        return entityManager.persist(category);
    }

    private CouponStateCode couponStateCodeDummy() {
        CouponStateCode couponStateCode = new CouponStateCode(null, COUPON_ALL.getName(),
                COUPON_ALL.isUsed(), "info");

        return entityManager.persist(couponStateCode);
    }
}