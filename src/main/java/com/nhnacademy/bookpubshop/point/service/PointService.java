package com.nhnacademy.bookpubshop.point.service;

import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.utils.PageResponse;
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
}
