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
    Optional<TierResponseDto> findTierName(String tierName);

    Optional<TierResponseDto> findTier(Integer tierNo);

    List<TierResponseDto> findTiers();

}
