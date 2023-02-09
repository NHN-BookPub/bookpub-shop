package com.nhnacademy.bookpubshop.coupontemplate.service;

import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * CouponTemplate 서비스 인터페이스입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public interface CouponTemplateService {
    /**
     * 쿠폰템플릿 단건 상세조회를 위한 메서드.
     *
     * @param templateNo 조회할 쿠폰템플릿 번호
     * @return GetDetailCouponTemplateResponseDto 쿠폰템플릿 조회 상세정보를 담은 Dto
     */
    GetDetailCouponTemplateResponseDto getDetailCouponTemplate(Long templateNo);

    /**
     * 쿠폰템플릿 단건 상세조회를 위한 메서드 --> 쿠폰명으로 조회.
     *
     * @param templateName 조회할 쿠폰템플릿 번호
     * @return GetDetailCouponTemplateResponseDto 쿠폰템플릿 조회 상세정보를 담은 Dto
     */
    CouponTemplate getCouponTemplateByName(String templateName);

    /**
     * 전체 쿠폰템플릿 조회를 위한 메서드.
     *
     * @param pageable 조회할 페이지 정보
     * @return 쿠폰템플릿 조회 정보를 담은 Dto 페이지
     */
    Page<GetCouponTemplateResponseDto> getCouponTemplates(Pageable pageable) throws IOException;

    /**
     * 쿠폰템플릿 생성을 위한 메서드.
     *
     * @param createRequestDto 생성할 쿠폰템플릿 정보를 담은 Dto,
     * @param image            쿠폰템플릿에 들어갈 이미지 파일
     */
    void createCouponTemplate(CreateCouponTemplateRequestDto createRequestDto,
                              MultipartFile image) throws IOException;

    /**
     * 쿠폰템플릿 수정을 위한 메서드.
     *
     * @param modifyRequestDto 수정할 쿠폰템플릿 정보를 담은 Dto
     */
    void modifyCouponTemplate(Long templateNo, ModifyCouponTemplateRequestDto modifyRequestDto,
                              MultipartFile image) throws IOException;

    /**
     * 파일 다운로드를 위한 메서드.
     *
     * @param templateNo 템플릿 번호
     * @return 오브젝트 스토리지 접근에 필요한 정보를 담은 Dto
     * @throws IOException the io exception
     */
    GetDownloadInfo downloadCouponTemplate(Long templateNo) throws IOException;
}
