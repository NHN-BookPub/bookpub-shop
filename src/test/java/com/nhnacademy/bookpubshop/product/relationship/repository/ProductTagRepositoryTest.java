package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductSaleState.SALE;
import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
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

        productTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        productTypeStateCodeRepository.save(productTypeStateCode);

        productSaleStateCode = new ProductSaleStateCode(null,
                SALE.getName(), SALE.isUsed(), "이 상품은 판매중입니다.");
        productSaleStateCodeRepository.save(productSaleStateCode);

        product = new Product(null, productPolicy, productTypeStateCode, productSaleStateCode,
                Collections.EMPTY_LIST, "00123-1111", "title", "publisher", 100, "설명",
                "썸네일.png", "eBook path", 20000L,10L,
                1, 1L, 3, false, 100,
                LocalDateTime.now(), LocalDateTime.now(), false);
        productRepository.save(product);

        tag = new Tag(null, "강추", "#FFFFFF");
        tagRepository.save(tag);
    }

    @Test
    @DisplayName(value = "상품태그(product_and_tag) 레포지토리 save 테스트")
    void productTagSaveTest() {
        ProductTag productTag = new ProductTag(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product);
        productTagRepository.save(productTag);

        Optional<ProductTag> optional = productTagRepository.findById(new ProductTag.Pk(tag.getTagNo(), product.getProductNo()));
        assertThat(optional).isPresent();
        assertThat(optional.get().getPk().getTagNo()).isEqualTo(tag.getTagNo());
        assertThat(optional.get().getPk().getProductNo()).isEqualTo(product.getProductNo());

        entityManager.clear();
    }
}