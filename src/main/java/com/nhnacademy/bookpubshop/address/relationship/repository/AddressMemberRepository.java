package com.nhnacademy.bookpubshop.address.relationship.repository;

import com.nhnacademy.bookpubshop.address.relationship.entity.AddressMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주소 멤버 관계 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface AddressMemberRepository extends JpaRepository<AddressMember, AddressMember.Pk> {

}
