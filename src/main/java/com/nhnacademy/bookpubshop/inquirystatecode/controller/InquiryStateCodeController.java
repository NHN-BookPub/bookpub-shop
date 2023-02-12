package com.nhnacademy.bookpubshop.inquirystatecode.controller;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.service.InquiryStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Controller
@RequiredArgsConstructor
public class InquiryStateCodeController {
    private final InquiryStateCodeService inquiryStateCodeService;

    @GetMapping("/api/inquiry-state-codes/member")
    public ResponseEntity<List<GetInquiryStateCodeResponseDto>> codeListForMember() {
        List<GetInquiryStateCodeResponseDto> content =
                inquiryStateCodeService.getUsedCodeForMember();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(content);
    }

    @GetMapping("/api/inquiry-state-codes/admin")
    public ResponseEntity<List<GetInquiryStateCodeResponseDto>> codeListForAdmin() {
        List<GetInquiryStateCodeResponseDto> content =
                inquiryStateCodeService.getUsedCodeForAdmin();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(content);
    }
}
