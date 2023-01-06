package com.nhnacademy.bookpubshop.tier.service;

import com.nhnacademy.bookpubshop.tier.dto.request.TierCreateRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.request.TierModifyRequestDto;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import java.util.List;

/**
 * Tier 에대한 Service 인터페이스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface TierService {

    /**
     * 등급을생성하기 위한 메소드입니다.
     *
     * @param tierCreateRequestDto 등급생성을위한정보들이 기입됩니다.
     * @author : 유호철
     */
    void addTier(TierCreateRequestDto tierCreateRequestDto);

    /**
     * 등급수정을위한 메서드입니다.
     *
     * @param tierModifyRequestDto 등급수정에대한 정보들이 기입됩니다.
     * @author : 유호철
     */
    void modifyTier(TierModifyRequestDto tierModifyRequestDto);

    /**
     * 등급에대한 단건조회를 위한 메소드입니다.
     *
     * @param tierNo 등급번호
     * @return TierResponseDto 등급이름이 반환됩니다.
     */
    TierResponseDto getTier(Integer tierNo);

    /**
     * 전체등급에 대한조회를 하는 메서드입니다.
     *
     * @return 전체 등급이 반환된다.
     */
    List<TierResponseDto> getTiers();
}
