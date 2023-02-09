package com.nhnacademy.bookpubshop.subscribe.repository;

import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 을 사용하기위한 interface 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@NoRepositoryBean
public interface SubscribeRepositoryCustom {
    /**
     * 구독들을 반환하기위한메서드 입니다.
     *
     * @param pageable 페이징 정보가 기입
     * @return 구독들이 반환됩니다.
     */
    Page<GetSubscribeResponseDto> getSubscribes(Pageable pageable);

    /**
     * 구독의 상세정보를 반환
     *
     * @param subscribeNo 구독번호.
     * @return 구독상세값 반환.
     */
    Optional<GetSubscribeDetailResponseDto> getSubscribeDetail(Long subscribeNo);
}
