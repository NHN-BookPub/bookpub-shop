package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 판매 유형 코드 커스텀 레포지토리 인터페이스.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@NoRepositoryBean
public interface ProductSaleStateCodeRepositoryCustom {
    List<GetProductSaleStateCodeResponseDto> findByAllUsed();
}
