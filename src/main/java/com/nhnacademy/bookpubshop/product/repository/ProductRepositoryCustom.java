package com.nhnacademy.bookpubshop.product.repository;

import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 상품 레포지토리에서 쿼리 dsl 을 사용하기 위한 커스텀 레포입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface ProductRepositoryCustom {
    List<GetProductListResponseDto> getAllProducts(Pageable pageable);

    List<GetProductListResponseDto> getProductListLikeTitle(String title, Pageable pageable);
}
