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

    /**
     * 관리자가 포인트내역을 회원 아이디를 검색하여 조회하기위한 메서드입니다.
     *
     * @param pageable 페이징
     * @param start 시작일
     * @param end 종료일
     * @param search 아이디 검색어
     * @return 포인트 내역
     */
    Page<GetPointAdminResponseDto> getPointsBySearch(Pageable pageable,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     String search);
}
