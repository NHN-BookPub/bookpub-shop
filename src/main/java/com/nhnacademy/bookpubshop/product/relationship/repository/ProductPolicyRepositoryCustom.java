package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductPolicyResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 정책 커스텀 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@NoRepositoryBean
public interface ProductPolicyRepositoryCustom {
    List<GetProductPolicyResponseDto> findAllPolicies();
}
