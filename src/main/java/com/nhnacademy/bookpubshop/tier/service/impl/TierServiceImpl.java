package com.nhnacademy.bookpubshop.tier.service.impl;

import com.nhnacademy.bookpubshop.tier.dto.request.CreateTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.ModifyTierRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierAlreadyExists;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import com.nhnacademy.bookpubshop.tier.service.TierService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TierService 구현 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class TierServiceImpl implements TierService {
    private final TierRepository tierRepository;

    /**
     * {@inheritDoc}
     *
     * @throws TierNotFoundException 등급이 없을때발생하는에러.
     */
    @Transactional
    @Override
    public void addTier(CreateTierRequestDto createTierRequestDto) {

        if (tierRepository.existsByTierName(createTierRequestDto.getTierName())) {
            throw new TierAlreadyExists(createTierRequestDto.getTierName());
        }

        tierRepository.save(BookPubTier.builder()
                .tierName(createTierRequestDto.getTierName())
                .tierPrice(createTierRequestDto.getTierPrice())
                .tierValue(createTierRequestDto.getTierValue())
                .tierPoint(createTierRequestDto.getTierPoint())
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyTier(ModifyTierRequestDto modifyTierRequestDto) {
        BookPubTier tier = tierRepository.findById(modifyTierRequestDto.getTierNo())
                .orElseThrow(TierNotFoundException::new);

        if (tierRepository.existsByTierName(modifyTierRequestDto.getTierName())) {
            throw new TierAlreadyExists(modifyTierRequestDto.getTierName());
        }

        tier.modifyTierName(modifyTierRequestDto.getTierName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TierResponseDto getTier(Integer tierNo) {
        return tierRepository.findTier(tierNo)
                .orElseThrow(TierNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<TierResponseDto> getTiers() {
        return tierRepository.findTiers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getTierName(String name) {
        return tierRepository.existsByTierName(name);
    }
}
