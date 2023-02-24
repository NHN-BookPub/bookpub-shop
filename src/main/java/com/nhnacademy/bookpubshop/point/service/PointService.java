package com.nhnacademy.bookpubshop.point.service;

import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

/**
 * 포인트 서비스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface PointService {

    /**
     * 포인트 내역 불러오는 메소드.
     *
     * @param pageable 페이지.
     * @param type 유형.
     * @param memberNo 멤버 번호.
     * @return 포인트내역.
     */
    PageResponse<GetPointResponseDto> getPointHistory(
            Pageable pageable, String type, Long memberNo);

    /**
     * 포인트 선물 메소드.
     *
     * @param memberNo 멤버 no.
     * @param giftRequestDto 선물정보 dto.
     */
    void giftPoint(Long memberNo, PointGiftRequestDto giftRequestDto);

    /**
     * 관리자 포인트 전체내역조회.
     *
     * @param pageable 페이징정보
     * @param start    시작일자
     * @param end      종료일자
     * @return 포인트내역 반환
     */
    PageResponse<GetPointAdminResponseDto> getPoints(Pageable pageable,
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
    PageResponse<GetPointAdminResponseDto> getPointsBySearch(Pageable pageable,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             String search);
}
