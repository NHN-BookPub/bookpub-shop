package com.nhnacademy.bookpubshop.authority.repository;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 권한테이블에 직접접근하기위한 Repo 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
