package com.nhnacademy.bookpubshop.wishlist.repository;

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
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    BookPubTier bookPubTier;
    Member member;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;

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

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
    }

    @Test
    @DisplayName(value = "위시리스트(wishlist) 레포지토리 save 테스트")
    void wishlistSaveTest() {
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
}