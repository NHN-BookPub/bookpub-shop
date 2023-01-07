package com.nhnacademy.bookpubshop.tier.repository;

import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Tier 가 querydsl 을 사용하기위한 interface 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@NoRepositoryBean
public interface TierRepositoryCustom {
    /**
     * 등급명을 통회 관련등급을 반환.
     *
     * @param tierName 등급명 기입.
     * @return optional 로되어있는 등급정보 반환.
     * @author : 유호철
     */
    Optional<TierResponseDto> findTierName(String tierName);

    /**
     * 등급번호를 통해 관련등급 정보를 반환.
     *
     * @param tierNo 등급번호 기입.
     * @return optional 로되어있는 등급정보 반환.
     * @author : 유호철
     */
    Optional<TierResponseDto> findTier(Integer tierNo);

    /**
     * 등급의 전체 정보를 반환.
     *
     * @return list 전체등급 정보반환.
     * @author : 유호철
     */
    List<TierResponseDto> findTiers();

}
