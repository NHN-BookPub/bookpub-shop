package com.nhnacademy.bookpubshop.point.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.point.dto.request.PointGiftRequestDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointAdminResponseDto;
import com.nhnacademy.bookpubshop.point.dto.response.GetPointResponseDto;
import com.nhnacademy.bookpubshop.point.service.PointService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 포인트 컨트롤러입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    /**
     * 해당 유저의 포인트 내역을 불러옵니다.
     *
     * @param pageable 페이저블.
     * @param type     포인트 전체, 사용, 적립
     * @param memberNo 유저번호.
     * @return 포인트 사용내역.
     */
    @GetMapping("/token/point/{memberNo}")
    @MemberAndAuth
    public ResponseEntity<PageResponse<GetPointResponseDto>> getPointHistory(
            Pageable pageable, @RequestParam String type, @PathVariable Long memberNo) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pointService.getPointHistory(pageable, type, memberNo));
    }

    /**
     * 유저 간 포인트 선물 메서드.
     *
     * @param giftRequestDto 요청 dto.
     * @return 성공 status.
     */
    @PostMapping("/token/point/{memberNo}")
    @MemberAndAuth
    public ResponseEntity<Void> giftPoint(@RequestBody PointGiftRequestDto giftRequestDto,
                                          @PathVariable Long memberNo) {
        pointService.giftPoint(memberNo, giftRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /**
     * 관리자가 포인트내역을 조회하기위한 메서드입니다.
     * 성공시 200이 반환됩니다.
     *
     * @param pageable 페이징
     * @param start    시작일자
     * @param end      종료일자
     * @return 포인트내역 반환.
     */
    @AdminAuth
    @GetMapping("/token/points")
    public ResponseEntity<PageResponse<GetPointAdminResponseDto>> getPoints(
            Pageable pageable,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pointService.getPoints(pageable, start, end));
    }

    /**
     * 관리자가 포인트내역을 회원 아이디를 검색하여 조회하기위한 메서드입니다.
     * 성공시 200이 반환됩니다.
     *
     * @param pageable 페이징
     * @param start 시작일
     * @param end 종료일
     * @param search 아이디 검색어
     * @return 포인트 내역
     */
    @AdminAuth
    @GetMapping("/token/points/{search}")
    public ResponseEntity<PageResponse<GetPointAdminResponseDto>> getPointsBySearch(
            Pageable pageable,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end,
            @PathVariable String search) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(pointService.getPointsBySearch(pageable, start, end, search));
    }
}
