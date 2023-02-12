package com.nhnacademy.bookpubshop.subscribe.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.SubscribeProductList;
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
 * 구독 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class SubscribeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    SubscribeRepository subscribeRepository;

    Subscribe subscribe;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;

    File file;
    File productFile;
    @BeforeEach
    void setUp() {
        productPolicy = ProductPolicyDummy.dummy();
        typeStateCode = ProductTypeStateCodeDummy.dummy();
        saleStateCode = ProductSaleStateCodeDummy.dummy();

        entityManager.persist(productPolicy);
        entityManager.persist(typeStateCode);
        entityManager.persist(saleStateCode);
        product = ProductDummy.dummy(productPolicy, typeStateCode, saleStateCode);

        entityManager.persist(product);
        subscribe = SubscribeDummy.dummy();
        file = FileDummy.dummy(null, null, null, null, null, subscribe);
        entityManager.persist(file.getSubscribe());
        entityManager.persist(file);

    }

    @Test
    @DisplayName("구독 save 테스트")
    void memberSaveTest() {
        Subscribe persist = entityManager.persist(subscribe);

        Optional<Subscribe> subscribe = subscribeRepository.findById(persist.getSubscribeNo());

        assertThat(subscribe).isPresent();
        assertThat(subscribe.get().getSubscribeNo()).isEqualTo(persist.getSubscribeNo());
        assertThat(subscribe.get().getSubscribeName()).isEqualTo(persist.getSubscribeName());
        assertThat(subscribe.get().getSubscribePrice()).isEqualTo(persist.getSubscribePrice());
        assertThat(subscribe.get().isSubscribeDeleted()).isEqualTo(persist.isSubscribeDeleted());
        assertThat(subscribe.get().isSubscribeRenewed()).isEqualTo(persist.isSubscribeRenewed());
        assertThat(subscribe.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(subscribe.get().getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(subscribe.get().getViewCount()).isEqualTo(persist.getViewCount());
    }

    @Test
    @DisplayName("구독 조회 테스트")
    void subscribeInfo() {
        PageRequest page = PageRequest.of(0, 10);
        Subscribe persist = file.getSubscribe();
        Page<GetSubscribeResponseDto> result = subscribeRepository.getSubscribes(page);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getSubscribeNo()).isEqualTo(persist.getSubscribeNo());
        assertThat(result.getContent().get(0).getSubscribeName()).isEqualTo(persist.getSubscribeName());
        assertThat(result.getContent().get(0).getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(result.getContent().get(0).getViewCnt()).isEqualTo(persist.getViewCount());
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(persist.getSubscribePrice());
        assertThat(result.getContent().get(0).getSalePrice()).isEqualTo(persist.getSalesPrice());
        assertThat(result.getContent().get(0).isDeleted()).isEqualTo(persist.isSubscribeDeleted());
        assertThat(result.getContent().get(0).isRenewed()).isEqualTo(persist.isSubscribeRenewed());

    }

    @Test
    @DisplayName("구독 연관 상품 조회 테스트")
    void subscribeRelationProductTest(){
        productFile = entityManager.persist(
                FileDummy.dummy(null, null,
                        null, product, null, FileCategory.PRODUCT_THUMBNAIL, null));
        entityManager.persist(product);
        subscribe.addRelationList(new SubscribeProductList(null, subscribe, product));

        Optional<GetSubscribeDetailResponseDto> result = subscribeRepository.getSubscribeDetail(subscribe.getSubscribeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getImagePath()).isEqualTo(file.getFilePath());
        assertThat(result.get().getSubscribeName()).isEqualTo(subscribe.getSubscribeName());
        assertThat(result.get().getSubscribeNo()).isEqualTo(subscribe.getSubscribeNo());
        assertThat(result.get().getPrice()).isEqualTo(subscribe.getSubscribePrice());
        assertThat(result.get().getSalePrice()).isEqualTo(subscribe.getSalesPrice());
        assertThat(result.get().getViewCnt()).isEqualTo(subscribe.getViewCount());
        assertThat(result.get().getProductLists().get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.get().getProductLists().get(0).getTitle()).isEqualTo(product.getTitle());

    }
}