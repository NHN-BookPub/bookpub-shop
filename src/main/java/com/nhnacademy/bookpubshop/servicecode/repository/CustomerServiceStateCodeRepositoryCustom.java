package com.nhnacademy.bookpubshop.servicecode.repository;

import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 고객서비스 상태코드에서 쿼리 dsl을 사용하기 위한 custom 레포입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CustomerServiceStateCodeRepositoryCustom {

    /**
     * 코드명으로 고객서비스상태코드를 조회합니다.
     *
     * @param codeName 코드명
     * @return 코드
     */
    Optional<CustomerServiceStateCode> getCustomerServiceStateCodeByName(String codeName);
}
