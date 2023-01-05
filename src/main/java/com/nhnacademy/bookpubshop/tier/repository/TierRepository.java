package com.nhnacademy.bookpubshop.tier.repository;

import com.nhnacademy.bookpubshop.tier.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 등급 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface TierRepository extends JpaRepository<Tier, Integer> {

}