package com.nhnacademy.bookpubshop.product.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품태그(product_and_tag) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductTagRepositoryTest {

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
    TagRepository tagRepository;

    @Autowired
    ProductTagRepository productTagRepository;

    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;
    Tag tag;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(null, "실구매가가기준", true, 5);
        productPolicyRepository.save(productPolicy);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        tag = new Tag(null, "강추", "#FFFFFF");
        tagRepository.save(tag);

        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
    }

    @Test
    @DisplayName(value = "상품태그(product_and_tag) 레포지토리 save 테스트")
    void productTagSaveTest() {
        ProductTag productTag = new ProductTag(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product);
        ProductTag persist = productTagRepository.save(productTag);

        Optional<ProductTag> optional = productTagRepository.findById(persist.getPk());
        assertThat(optional).isPresent();
        assertThat(optional.get().getPk()).isEqualTo(persist.getPk());
        assertThat(optional.get().getPk().getTagNo()).isEqualTo(persist.getPk().getTagNo());
        assertThat(optional.get().getPk().getProductNo()).isEqualTo(persist.getPk().getProductNo());
        assertThat(optional.get().getTag().getTagNo()).isEqualTo(persist.getTag().getTagNo());
        assertThat(optional.get().getProduct().getProductNo()).isEqualTo(persist.getProduct().getProductNo());

        entityManager.clear();
    }
}