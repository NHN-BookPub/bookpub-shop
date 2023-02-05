package com.nhnacademy.bookpubshop.coupontemplate.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.service.CouponTemplateService;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 쿠폰템플릿 RestController 입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CouponTemplateController {
    private final CouponTemplateService couponTemplateService;

    /**
     * 쿠폰템플릿의 자세한 정보를 조회하는 메서드입니다. (관리자용)
     *
     * @param templateNo 조회할 쿠폰템플릿 번호
     * @return 성공 경우 200, 쿠폰템플릿의 자세한 정보 응답
     */
    @AdminAuth
    @GetMapping("/token/coupon-templates/{templateNo}")
    public ResponseEntity<GetDetailCouponTemplateResponseDto> couponTemplateDetail(
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
    @AdminAuth
    @GetMapping("/token/coupon-templates")
    public ResponseEntity<PageResponse<GetCouponTemplateResponseDto>>
    couponTemplateList(Pageable pageable) throws IOException {

        Page<GetCouponTemplateResponseDto> content =
                couponTemplateService.getCouponTemplates(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(content));
    }

    /**
     * 쿠폰템플릿을 등록하는 메서드입니다.
     *
     * @param request 등록할 정보를 담은 Dto
     * @param image   등록할 이미지 파일
     * @return 성공 경우 201
     */
    @AdminAuth
    @PostMapping("/token/coupon-templates")
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
     * @param image   수정할 이미지 파일
     * @return 성공 경우 201
     */
    @AdminAuth
    @PutMapping("/token/coupon-templates/{templateNo}")
    public ResponseEntity<Void> couponTemplateModify(
            @PathVariable("templateNo") Long templateNo,
            @Valid @RequestPart("modifyRequestDto") ModifyCouponTemplateRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        couponTemplateService.modifyCouponTemplate(templateNo, request, image);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 쿠폰 템플릿 이미지 다운로드를 위한 메서드입니다.
     * 다운로드 예시 메서드이며, 추후 삭제 예정입니다.
     *
     * @param templateNo 쿠폰템플릿 번호
     * @return 다운로드에 필요한 정보를 담은 Dto
     * @throws IOException 파일 관련 exception
     */
    @AdminAuth
    @GetMapping("/token/coupon-templates/{templateNo}/download")
    public ResponseEntity<GetDownloadInfo> couponTemplateDownload(
            @PathVariable("templateNo") Long templateNo) throws IOException {
        GetDownloadInfo info = couponTemplateService.downloadCouponTemplate(templateNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(info);
    }
}
