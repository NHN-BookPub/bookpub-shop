package com.nhnacademy.bookpubshop.point.repository;

import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 포인트 내역 레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface PointHistoryRepositoryCustom {
    Page<GetPointResponseDto> getPointHistory(
            Pageable pageable, String type, Long memberNo);
}
