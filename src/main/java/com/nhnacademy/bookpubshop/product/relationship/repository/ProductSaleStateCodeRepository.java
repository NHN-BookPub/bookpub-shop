package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품판매여부코드(product_sale_state_code) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface ProductSaleStateCodeRepository
        extends JpaRepository<ProductSaleStateCode, Integer>, ProductSaleStateCodeRepositoryCustom {
    /**
     * 상태코드명으로 상태코드를 조회합니다.
     *
     * @param codeCategory 코드명
     * @return 상태코드
     */
    Optional<ProductSaleStateCode> findByCodeCategory(String codeCategory);
}