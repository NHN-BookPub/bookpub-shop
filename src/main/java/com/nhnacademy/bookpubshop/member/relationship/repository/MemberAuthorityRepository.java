package com.nhnacademy.bookpubshop.member.relationship.repository;

import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 멤버 권한 연관관계 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface MemberAuthorityRepository
        extends JpaRepository<MemberAuthority, MemberAuthority.Pk> {

}
