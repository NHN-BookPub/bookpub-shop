package com.nhnacademy.bookpubshop.tier.repository.impl;

import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.entity.QBookPubTier;
import com.nhnacademy.bookpubshop.tier.repository.TierRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 회원등급 Custom Repository 구현체.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class TierRepositoryImpl extends QuerydslRepositorySupport
        implements TierRepositoryCustom {

    public TierRepositoryImpl() {
        super(BookPubTier.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TierResponseDto> findTierName(String tierName) {
        QBookPubTier tier = QBookPubTier.bookPubTier;

        return Optional.of(from(tier)
                .where(tier.tierName.eq(tierName))
                .select(Projections
                        .constructor(TierResponseDto.class,
                                tier.tierNo,
                                tier.tierName,
                                tier.tierValue,
                                tier.tierPrice,
                                tier.tierPoint))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TierResponseDto> findTier(Integer tierNo) {
        QBookPubTier tier = QBookPubTier.bookPubTier;
        return Optional.of(from(tier)
                .where(tier.tierNo.eq(tierNo))
                .select(Projections
                        .constructor(TierResponseDto.class,
                                tier.tierNo,
                                tier.tierName,
                                tier.tierValue,
                                tier.tierPrice,
                                tier.tierPoint))
                .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TierResponseDto> findTiers() {
        QBookPubTier tier = QBookPubTier.bookPubTier;

        return from(tier)
                .select(Projections
                        .constructor(TierResponseDto.class,
                                tier.tierNo,
                                tier.tierName,
                                tier.tierValue,
                                tier.tierPrice,
                                tier.tierPoint))
                .orderBy(tier.tierValue.asc())
                .fetch();
    }
}
