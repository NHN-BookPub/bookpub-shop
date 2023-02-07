package com.nhnacademy.bookpubshop.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.customersupport.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
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
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 위시리스트(wishlist) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class WishlistRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    ProductPolicyRepository productPolicyRepository;
//
//    @Autowired
//    ProductTypeStateCodeRepository productTypeStateCodeRepository;
//
//    @Autowired
//    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    BookPubTier bookPubTier;
    Member member;
    Product product;
    File file;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    PersonalInquiry personalInquiry;
    Review review;
    ReviewPolicy reviewPolicy;
    CouponTemplate couponTemplate;
    CouponPolicy couponPolicy;
    CouponType couponType;
    Category category;
    CouponStateCode couponStateCode;
    CustomerService customerService;
    CustomerServiceStateCode customerServiceStateCode;


    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        personalInquiry = PersonalInquiryDummy.dummy(member);
        reviewPolicy = ReviewPolicyDummy.dummy();
        review = ReviewDummy.dummy(member, product, reviewPolicy);
        couponPolicy = CouponPolicyDummy.dummy();
        couponType = CouponTypeDummy.dummy();
        category = CategoryDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category, couponStateCode);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);
//        file = new File(null, review,
//                personalInquiry, couponTemplate,
//                product, customerService,
//                FileCategory.PRODUCT_THUMBNAIL.getCategory(),
//                "path", ".png",
//                "origin", "a");


    }

    @Test
    @DisplayName(value = "위시리스트(wishlist) 레포지토리 save 테스트")
    void wishlistSaveTest() {
        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(personalInquiry);
        entityManager.persist(reviewPolicy);
        entityManager.persist(review);
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(category);
        entityManager.persist(couponStateCode);
        entityManager.persist(couponTemplate);
        entityManager.persist(customerServiceStateCode);
        entityManager.persist(customerService);

        Wishlist wishlist = new Wishlist(new Wishlist.Pk(member.getMemberNo(), product.getProductNo()),
                member, product, true);
        Wishlist persist = wishlistRepository.save(wishlist);

        Optional<Wishlist> optional = wishlistRepository.findById(persist.getPk());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPk().getMemberNo()).isEqualTo(persist.getMember().getMemberNo());
        assertThat(optional.get().getPk().getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(optional.get().getPk()).isEqualTo(persist.getPk());
        assertThat(optional.get().isWishlistApplied()).isTrue();

        entityManager.clear();
    }

    @Test
    @DisplayName("멤버별 위시리스트 조회 테스트")
    void getWishlistByMember() {
        // given
        entityManager.persist(bookPubTier);
        Member memberDummy = entityManager.persist(member);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        entityManager.persist(product.getRelationProduct().get(0));
        entityManager.persist(personalInquiry);
        entityManager.persist(reviewPolicy);
        entityManager.persist(review);
        entityManager.persist(couponPolicy);
        entityManager.persist(couponType);
        entityManager.persist(category);
        entityManager.persist(couponStateCode);
        entityManager.persist(couponTemplate);
        entityManager.persist(customerServiceStateCode);
        entityManager.persist(customerService);

//        File fileDummy = entityManager.persist(
//                new File(null, null, null, null, product,
//                        null, "thumbnail", "path", ".exe", "origin", "name"));

        product.setProductFiles(List.of(file));


        Wishlist wishlist = new Wishlist(new Wishlist.Pk(member.getMemberNo(), product.getProductNo()),
                member, product, true);
        Wishlist persist = entityManager.persist(wishlist);

        // when
        Page<GetWishlistResponseDto> result = wishlistRepository.findWishlistByMember(PageRequest.of(0, 10), memberDummy.getMemberNo());
        List<GetWishlistResponseDto> list = result.getContent();

        // then
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getProductNo()).isEqualTo(persist.getProduct().getProductNo());
        assertThat(list.get(0).getTitle()).isEqualTo(persist.getProduct().getTitle());
        assertThat(list.get(0).getProductPublisher()).isEqualTo(persist.getProduct().getProductPublisher());
//        assertThat(list.get(0).getThumbnail()).isEqualTo(fileDummy.getFilePath());
        assertThat(list.get(0).isWishlistApplied()).isEqualTo(persist.isWishlistApplied());
    }
}