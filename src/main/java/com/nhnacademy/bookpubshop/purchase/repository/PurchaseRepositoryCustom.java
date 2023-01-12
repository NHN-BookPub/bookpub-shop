package com.nhnacademy.bookpubshop.purchase.repository;

import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseResponseDto;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 구매이력 레포지토리에서 쿼리 dsl을 사용하기 위한 custom 레포입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PurchaseRepositoryCustom {
    List<GetPurchaseResponseDto> findByProductNumber(Long number);


}
