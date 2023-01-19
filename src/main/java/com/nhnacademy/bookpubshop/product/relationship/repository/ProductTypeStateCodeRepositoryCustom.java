package com.nhnacademy.bookpubshop.product.relationship.repository;

import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 유형 코드 커스템 레포지토리 인터페이스.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@NoRepositoryBean
public interface ProductTypeStateCodeRepositoryCustom {

    List<GetProductTypeStateCodeResponseDto> findByAllUsed();

}
