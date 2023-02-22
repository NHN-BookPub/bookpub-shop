package com.nhnacademy.bookpubshop.coupon.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.coupon.dto.request.CreateCouponRequestDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.dto.response.GetOrderCouponResponseDto;
import com.nhnacademy.bookpubshop.coupon.service.CouponService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쿠폰 RestController 입니다.
 *
 * @author : 정유진, 김서현
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 전체 리스트를 조회하는 메서드입니다.
     * 관리자만 사용하는 메서드입니다.
     *
     * @return 성공 경우 200, 태그 페이지 응답
     */
    @AdminAuth
    @GetMapping("/token/coupons")
    public ResponseEntity<PageResponse<GetCouponResponseDto>>
        couponList(Pageable pageable,
               @RequestParam(value = "searchKey", required = false) String searchKey,
               @RequestParam(value = "search", required = false) String search)
            throws IOException {
        Page<GetCouponResponseDto> content =
                couponService.getCoupons(pageable, searchKey, search);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 단건 쿠폰을 조회하는 메서드입니다.
     *
     * @param couponNo 쿠폰을 조회하기 위한 쿠폰번호
     * @return 성공 경우 200, 태그 정보 응답
     */
    @MemberAuth
    @GetMapping("/token/coupons/{couponNo}")
    public ResponseEntity<GetCouponResponseDto> couponDetail(
            @PathVariable("couponNo") Long couponNo) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponService.getCoupon(couponNo));
    }

    /**
     * 쿠폰 등록을 하는 메서드입니다.
     *
     * @param request 쿠폰 생성을 위한 쿠폰 정보 Dto
     * @return 성공 경우 201 응답
     */
    @AdminAuth
    @PostMapping("/token/coupons")
    public ResponseEntity<Void> couponAdd(@Valid @RequestBody CreateCouponRequestDto request) {
        couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 쿠폰의 사용여부를 수정하기 위한 메소드입니다.
     *
     * @param couponNo 수정할 쿠폰 번호
     * @return 성공 경우 201 응답
     */
    @AdminAuth
    @PutMapping("/token/coupons/{couponNo}/used")
    public ResponseEntity<Void> couponModifyUsed(@PathVariable("couponNo") Long couponNo) {
        couponService.modifyCouponUsed(couponNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 주문에 필요한 쿠폰을 조회하는 메서드입니다.
     *
     * @param memberNo  멤버 번호
     * @param productNo 상품 번호 리스트
     * @return 사용할 수 있는 쿠폰 리스트 반환
     */
    @GetMapping("/api/coupons/members/{memberNo}/order")
    public ResponseEntity<List<GetOrderCouponResponseDto>> orderCouponList(
            @PathVariable("memberNo") Long memberNo,
            @RequestParam("productNo") Long productNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponService.getOrderCoupons(memberNo, productNo));
    }

    /**
     * 마이페이지에서 사용가능한 쿠폰을 조회하는 메소드입니다.
     *
     * @param memberNo 멤버번호
     * @param pageable 페이지
     * @return 사용할 수 있는 쿠폰 리스트 반환
     */
    @MemberAuth
    @GetMapping("/token/coupons/members/{memberNo}/positive")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> memberPositiveCouponList(
            @PathVariable("memberNo") Long memberNo, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(couponService.getPositiveCouponList(pageable, memberNo)));
    }

    /**
     * 마이페이지에서 사용불가능한 쿠폰을 조회하는 메소드입니다.
     *
     * @param memberNo 멤버번호
     * @param pageable 페이지
     * @return 사용불가능한 쿠폰 리스트 반환
     */
    @MemberAuth
    @GetMapping("/token/coupons/members/{memberNo}/negative")
    public ResponseEntity<PageResponse<GetCouponResponseDto>> memberNegativeCouponList(
            @PathVariable("memberNo") Long memberNo, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(couponService.getNegativeCouponList(pageable, memberNo)));
    }

    /**
     * 회원이 포인트 쿠폰 사용 시 상태변경 및 포인트 적립을 위한 메서드입니다.
     *
     * @param couponNo 쿠폰 번호
     * @param memberNo 회원 번호
     * @return the response entity
     */
    @MemberAuth
    @PutMapping("/token/coupons/{couponNo}/point/members/{memberNo}")
    public ResponseEntity<Void> pointCouponModifyUsed(@PathVariable("couponNo") Long couponNo,
                                                      @PathVariable("memberNo") Long memberNo) {
        couponService.modifyPointCouponUsed(couponNo, memberNo);

        return ResponseEntity.ok().build();
    }

    /**
     * 멤버의 등급쿠폰 발급 유무를 확인하는 메서드입니다.
     *
     * @param memberNo    멤버 번호
     * @param tierCoupons 등급 쿠폰 리스트
     * @return 발급 유무
     */
    @MemberAuth
    @GetMapping("/token/coupons/{memberNo}/tier-coupons")
    public ResponseEntity<Boolean> existsCouponListByMemberNo(
            @PathVariable Long memberNo,
            @RequestParam List<Long> tierCoupons) {

        boolean check = couponService.existsCouponsByMemberNo(memberNo, tierCoupons);

        return ResponseEntity.status(HttpStatus.OK)
                .body(check);
    }

    /**
     * 멤버에게 등급 쿠폰을 발급하는 메서드입니다.
     *
     * @param memberNo    멤버 번호
     * @param tierCoupons 등급 쿠폰 리스트
     * @return 발급 결과
     */
    @MemberAuth
    @PostMapping("/token/coupons/{memberNo}/tier-coupons")
    public ResponseEntity<Void> issueTierCoupons(@PathVariable Long memberNo,
            @RequestParam List<Long> tierCoupons) {
        couponService.issueTierCouponsByMemberNo(memberNo, tierCoupons);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();

    }

    /**
     * 이달의 쿠폰 발급을 위한 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param templateNo 쿠폰 템플릿 번호
     * @return 성공일 경우 201반환. 상태값 반환
     */
    @MemberAuth
    @PostMapping("/token/coupons/{memberNo}/month-coupon")
    public ResponseEntity<String> issueMonthCoupon(@PathVariable Long memberNo,
            @RequestParam Long templateNo) throws IOException {

        couponService.issueCouponMonth(memberNo, templateNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();

    }

    /**
     * 이달의 쿠폰 발행 여부 확인을 위한 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param templateNo 쿠폰 템플릿 번호
     * @return 발행 여부
     */
    @MemberAuth
    @GetMapping("/token/coupons/{memberNo}/month-coupon")
    public ResponseEntity<Boolean> checkCouponMonthIssued(
            @PathVariable Long memberNo,
            @RequestParam Long templateNo) {

        boolean check = couponService.existsCouponMonthIssued(memberNo, templateNo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(check);
    }

    /**
     * 이달의 쿠폰 중복 발급을 확인하기 위한 메서드입니다.
     *
     * @param memberNo   멤버 번호
     * @param couponList 이달의 쿠폰 리스트
     * @return 발행 여부
     */
    @MemberAuth
    @GetMapping("/token/coupons/{memberNo}/month-coupons/issue-check")
    public ResponseEntity<List<Boolean>> checkCouponMonthListIssued(
            @PathVariable Long memberNo,
            @RequestParam List<Long> couponList
    ) {
        List<Boolean> result = couponService.existsCouponMonthListIssued(memberNo, couponList);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

}