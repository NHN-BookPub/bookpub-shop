package com.nhnacademy.bookpubshop.member.relationship.repository;

import com.nhnacademy.bookpubshop.member.relationship.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 멤버권한을 DB 에서 관리위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface MemberAuthorityRepository extends
        JpaRepository<MemberAuthority, MemberAuthority.Pk> {
}
