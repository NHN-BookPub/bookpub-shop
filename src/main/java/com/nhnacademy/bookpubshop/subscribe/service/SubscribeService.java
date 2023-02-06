package com.nhnacademy.bookpubshop.subscribe.service;

import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구독상품 관련 서비스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public interface SubscribeService {
    /**
     * 구독을 생성하기위한 메서드입니다.
     *
     * @param dto 구독생성 정보들 기
     */
    void createSubscribe(CreateSubscribeRequestDto dto);

    /**
     * 구독을 삭제하기위한 메서드입니다.
     *
     * @param subscribeNo 구독번호
     * @param used        사용여부
     */
    void deleteSubscribe(Long subscribeNo, boolean used);

    /**
     * 구독상품들이 반환됩니다.
     *
     * @param pageable 페이징 객체 기입
     * @return 구독정보들이 반환
     */
    Page<GetSubscribeResponseDto> getSubscribes(Pageable pageable);
}
