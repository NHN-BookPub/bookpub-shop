package com.nhnacademy.bookpubshop.tier.service.impl;

import com.nhnacademy.bookpubshop.tier.dto.request.TierCreateRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.TierModifyRequestDto;
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
    public void addTier(TierCreateRequestDto tierCreateRequestDto) {

        if (tierRepository.existsByTierName(tierCreateRequestDto.getTierName())) {
            throw new TierAlreadyExists(tierCreateRequestDto.getTierName());
        }

        tierRepository.save(new BookPubTier(tierCreateRequestDto.getTierName()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyTier(TierModifyRequestDto tierModifyRequestDto) {
        BookPubTier tier = tierRepository.findById(tierModifyRequestDto.getTierNo())
                .orElseThrow(TierNotFoundException::new);

        if (tierRepository.existsByTierName(tierModifyRequestDto.getTierName())) {
            throw new TierAlreadyExists(tierModifyRequestDto.getTierName());
        }

        tier.modifyTierName(tierModifyRequestDto.getTierName());
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
}
