package com.nhnacademy.bookpubshop.coupontemplate.controller;

import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 쿠폰템플릿 RestController 입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponTemplateController {
    private final CouponTemplateService couponTemplateService;

    /**
     * 쿠폰템플릿의 자세한 정보를 조회하는 메서드입니다. (관리자용)
     *
     * @param templateNo 조회할 쿠폰템플릿 번호
     * @return 성공 경우 200, 쿠폰템플릿의 자세한 정보 응답
     */
    @GetMapping("/coupon-templates/details/{templateNo}")
    public ResponseEntity<RestGetDetailCouponTemplateResponseDto> couponTemplateDetail(
            @PathVariable("templateNo") Long templateNo) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(couponTemplateService.getDetailCouponTemplate(templateNo));
    }

    /**
     * 전체 쿠폰템플릿을 조회하는 메서드입니다. (관리자용)
     *
     * @param pageable 페이지
     * @return 성공 경우 200, 쿠폰템플릿의 정보 페이지 응답
     */
    @GetMapping("/coupon-templates")
    public ResponseEntity<PageResponse<RestGetCouponTemplateResponseDto>>
    couponTemplateList(Pageable pageable) throws IOException {

        Page<RestGetCouponTemplateResponseDto> content =
                couponTemplateService.getCouponTemplates(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 쿠폰템플릿을 등록하는 메서드입니다.
     * 추후 작성이 필요합니다. (파일 관련 미완성)
     *
     * @return 성공 경우 201
     */
    @PostMapping(value = "/coupon-templates")
    public ResponseEntity<Void> couponTemplateAdd(
            @Valid @RequestPart("createRequestDto") CreateCouponTemplateRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        couponTemplateService.createCouponTemplate(request, image);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 쿠폰템플릿을 수정하는 메서드입니다.
     *
     * @param request 수정할 정보를 담은 Dto
     * @return 성공 경우 201
     */
    @PutMapping("/coupon-templates/{templateNo}")
    public ResponseEntity<Void> couponTemplateModify(
            @PathVariable("templateNo") Long templateNo,
            @Valid @RequestPart("modifyRequestDto") ModifyCouponTemplateRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        couponTemplateService.modifyCouponTemplate(templateNo, request, image);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
