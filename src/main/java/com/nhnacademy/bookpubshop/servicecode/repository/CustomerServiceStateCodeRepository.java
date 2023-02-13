package com.nhnacademy.bookpubshop.servicecode.repository;

import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 커스터머서비스 상태코드를 DB 와연결하기위한 Repo.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface CustomerServiceStateCodeRepository
        extends JpaRepository<CustomerServiceStateCode, Integer>,
        CustomerServiceStateCodeRepositoryCustom {

}
