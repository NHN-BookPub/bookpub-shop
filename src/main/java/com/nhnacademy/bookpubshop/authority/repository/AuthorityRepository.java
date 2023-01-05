package com.nhnacademy.bookpubshop.authority.repository;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 권한 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
