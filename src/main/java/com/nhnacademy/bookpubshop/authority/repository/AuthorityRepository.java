package com.nhnacademy.bookpubshop.authority.repository;

import com.nhnacademy.bookpubshop.authority.entity.Authority;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 권한 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    /** 권한의 이름으로 권한객체 가져오는 메서드.
     *
     * @param name 권한이름.
     * @return 권한객체를 반환합니다.
     */
    Optional<Authority> findByAuthorityName(String name);
}
