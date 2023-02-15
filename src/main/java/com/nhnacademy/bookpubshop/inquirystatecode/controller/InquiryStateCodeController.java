package com.nhnacademy.bookpubshop.inquirystatecode.controller;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.service.InquiryStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품문의상태코드를 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class InquiryStateCodeController {
    private final InquiryStateCodeService inquiryStateCodeService;

    /**
     * 회원이 사용할 수 있는 상품문의 코드정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의 코드정보가 담긴 dto 리스트
     */
    @GetMapping("/api/inquiry-state-codes/member")
    public ResponseEntity<List<GetInquiryStateCodeResponseDto>> codeListForMember() {
        List<GetInquiryStateCodeResponseDto> content =
                inquiryStateCodeService.getUsedCodeForMember();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(content);
    }

    /**
     * 관리자가 사용할 수 있는 상품문의 코드정보 리스트를 조회하기 위한 메서드입니다.
     *
     * @return 상품문의 코드정보가 담긴 dto 리스트
     */
    @GetMapping("/api/inquiry-state-codes/admin")
    public ResponseEntity<List<GetInquiryStateCodeResponseDto>> codeListForAdmin() {
        List<GetInquiryStateCodeResponseDto> content =
                inquiryStateCodeService.getUsedCodeForAdmin();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(content);
    }
}
