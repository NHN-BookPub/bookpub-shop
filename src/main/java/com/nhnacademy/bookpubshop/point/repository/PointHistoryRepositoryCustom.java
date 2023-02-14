package com.nhnacademy.bookpubshop.point.repository;

import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 포인트 내역 레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 */
@NoRepositoryBean
public interface PointHistoryRepositoryCustom {
    /**
     * 페이지 내역 조회하는 메소드.
     *
     * @param pageable 페이저블.
     * @param type     타입.
     * @param memberNo 회원번호.
     * @return 포인트내역 페이지.
     */
    Page<GetPointResponseDto> getPointHistory(
            Pageable pageable, String type, Long memberNo);

    /**
     * 관리자가 포인트 내역을 조회하는 메서드입니다.
     *
     * @param pageable 페이징
     * @return 포인트내역 반환.
     */
    Page<GetPointAdminResponseDto> getPoints(Pageable pageable,
                                             LocalDateTime start,
                                             LocalDateTime end);
}
