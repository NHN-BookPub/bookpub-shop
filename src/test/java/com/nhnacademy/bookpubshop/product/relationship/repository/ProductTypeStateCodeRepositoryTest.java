package com.nhnacademy.bookpubshop.product.relationship.repository;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 상품유형코드(product_type_state_code) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class ProductTypeStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Test
    @DisplayName(value = "상품유형코드(product_type_state_code) 레포지토리 save 테스트")
    void productTypeStateCodeSaveTest() {
        ProductTypeStateCode testProductTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        ProductTypeStateCode persist = productTypeStateCodeRepository.save(testProductTypeStateCode);

        Optional<ProductTypeStateCode> optional = productTypeStateCodeRepository.findById(persist.getCodeNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(optional.get().getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(optional.get().isCodeUsed()).isEqualTo(persist.isCodeUsed());
        assertThat(optional.get().getCodeInfo()).isEqualTo(persist.getCodeInfo());

        entityManager.clear();
    }

    @Test
    @DisplayName("사용 중인 코드 조회 테스트")
    void getUsedCode() {
        // given
        ProductTypeStateCode testProductTypeStateCode = new ProductTypeStateCode(null,
                BEST_SELLER.getName(), BEST_SELLER.isUsed(), "이 책은 베스트셀러입니다.");
        ProductTypeStateCode persist = productTypeStateCodeRepository.save(testProductTypeStateCode);

        // when
        List<GetProductTypeStateCodeResponseDto> allCodes = productTypeStateCodeRepository.findByAllUsed();

        // then
        assertThat(allCodes.get(0).getCodeNo()).isEqualTo(testProductTypeStateCode.getCodeNo());
        assertThat(allCodes.get(0).getCodeName()).isEqualTo(testProductTypeStateCode.getCodeName());
        assertThat(allCodes.get(0).isCodeUsed()).isEqualTo(testProductTypeStateCode.isCodeUsed());
        assertThat(allCodes.get(0).getCodeInfo()).isEqualTo(testProductTypeStateCode.getCodeInfo());

    }

}