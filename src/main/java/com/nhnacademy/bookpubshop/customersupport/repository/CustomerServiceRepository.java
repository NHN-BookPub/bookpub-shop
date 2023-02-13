package com.nhnacademy.bookpubshop.customersupport.repository;

import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 고객서비스가 DB 와 연결되기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface CustomerServiceRepository extends JpaRepository<CustomerService, Integer>, CustomerServiceRepositoryCustom {
}
