package com.nhnacademy.bookpubshop.inquiry.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.service.InquiryService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품문의를 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    /**
     * 해당 회원이 해당 상품의 구매 이력을 갖고 있는지 확인하기 위한 메서드입니다.
     * 회원이 상품문의 작성을 시도할 때 사용되는 메서드입니다.
     * 성공 시 200 반환
     *
     * @param memberNo  회원 번호
     * @param productNo 상품 번호
     * @return the response entity
     */
    @MemberAuth
    @GetMapping("/token/inquiries/members/{memberNo}/verify")
    public ResponseEntity<Boolean> writableInquiryVerify(
            @PathVariable("memberNo") Long memberNo, @RequestParam("productNo") Long productNo) {
        boolean result = inquiryService.verifyWritableInquiry(memberNo, productNo);

        return ResponseEntity.ok().body(result);
    }

    /**
     * 상품문의를 등록하기 위한 메서드입니다.
     * 성공 시 201 반환
     *
     * @param memberNo 회원 번호
     * @param request  등록할 상품문의 정보가 담긴 dto
     * @return the response entity
     */
    @MemberAndAuth
    @PostMapping("/token/inquiries/members/{memberNo}")
    public ResponseEntity<Void> inquiryAdd(@PathVariable Long memberNo,
                                           @Valid @RequestBody CreateInquiryRequestDto request) {
        inquiryService.createInquiry(memberNo, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품문의의 이미지를 추가하기 위한 메서드입니다.
     * tui editor 이미지 저장시 사용됩니다.
     * 성공 시 201 반환
     *
     * @param image 저장할 이미지파일
     * @return the response entity
     * @throws IOException 이미지 저장 오류 시 나는 에러
     */
    @MemberAndAuth
    @PostMapping("/token/inquiries/members/{memberNo}/image")
    public ResponseEntity<String> inquiryAddImage(
            @RequestPart("image") MultipartFile image) throws IOException {
        String path = inquiryService.addInquiryImage(image);

        return ResponseEntity.status(HttpStatus.CREATED).body(path);
    }


    /**
     * 상품문의의 답변을 삭제하기 위한 메서드입니다.
     * 성공 시 200 반환
     *
     * @param inquiryNo 삭제할 상품문의 번호
     * @return the response entity
     */
    @AdminAuth
    @DeleteMapping("/token/inquiries/{inquiryNo}/answer")
    public ResponseEntity<Void> inquiryAnswerCancel(@PathVariable Long inquiryNo) {
        inquiryService.deleteInquiryAnswer(inquiryNo);

        return ResponseEntity.ok().build();
    }

    /**
     * 상품문의의 문의를 삭제하기 위한 메서드입니다.
     * 문의를 삭제할 시, 답변도 함께 삭제됩니다.
     * 성공 시 200 반환
     *
     * @param inquiryNo 삭제할 상품문의 번호
     * @return the response entity
     */
    @MemberAuth
    @DeleteMapping("/token/inquiries/{inquiryNo}/members/{memberNo}")
    public ResponseEntity<Void> inquiryCancel(@PathVariable Long inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);

        return ResponseEntity.ok().build();
    }

    /**
     * 상품문의 답변 여부를 수정하기 위한 메서드입니다.
     * 관리자가 사용하는 메서드입니다.
     * 성공 시 200 반환
     *
     * @param inquiryNo 답변 여부를 수정할 상품문의번호
     * @return the response entity
     */
    @AdminAuth
    @PutMapping("/token/inquiries/{inquiryNo}/complete")
    public ResponseEntity<Void> inquiryComplete(@PathVariable Long inquiryNo) {
        inquiryService.modifyCompleteInquiry(inquiryNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 특정 상품의 상품문의 요약 정보를 조회하기 위한 메서드입니다.
     * 상품문의 제목, 답변 여부, 공개 여부 등이 조회됩니다.
     * 성공 시 200 반환
     *
     * @param pageable  상품문의의 리스트가 담긴 페이징 정보
     * @param productNo 상품문의를 조회할 상품 번호
     * @return the response entity
     */
    @GetMapping("/api/inquiries/products/{productNo}")
    public ResponseEntity<PageResponse<GetInquirySummaryProductResponseDto>>
        productInquiryList(Pageable pageable,
                       @PathVariable("productNo") Long productNo) {
        Page<GetInquirySummaryProductResponseDto> inquiries =
                inquiryService.getSummaryInquiriesByProduct(pageable, productNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(inquiries));
    }

    /**
     * 모든 상품문의의 요약 정보를 조회하기 위한 메서드입니다. (불량상품 문의 제외)
     * 관리자가 사용하는 메서드로서, 제목, 답변여부, 공개여부 외에도 검색을 위한 컬럼들이 포함되어 조회됩니다.
     * 성공 시 200 반환
     *
     * @param pageable       상품문의의 리스트가 담긴 페이징 정보
     * @param searchKeyFir   검색 조건 키
     * @param searchValueFir 검색 값
     * @param searchKeySec   검색 조건 키 두번째
     * @param searchValueSec 검색 값 두번째
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/inquiries")
    public ResponseEntity<PageResponse<GetInquirySummaryResponseDto>> inquiryList(
            Pageable pageable,
            @RequestParam(value = "searchKeyFir", required = false) String searchKeyFir,
            @RequestParam(value = "searchValueFir", required = false) String searchValueFir,
            @RequestParam(value = "searchKeySec", required = false) String searchKeySec,
            @RequestParam(value = "searchValueSec", required = false) String searchValueSec) {

        Page<GetInquirySummaryResponseDto> inquiries = inquiryService.getSummaryInquiries(pageable,
                searchKeyFir, searchValueFir, searchKeySec, searchValueSec);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(inquiries));
    }

    /**
     * 불량상품 문의를 조회하기 위한 메서드입니다.
     * 관리자가 사용하는 메서드입니다.
     *
     * @param pageable 불량상품 문의 리스트가 담긴 페이징정보
     * @return the response entity
     */
    @AdminAuth
    @GetMapping("/token/inquiries/error")
    public ResponseEntity<PageResponse<GetInquirySummaryResponseDto>> errorInquiryList(
            Pageable pageable) {

        Page<GetInquirySummaryResponseDto> inquiries =
                inquiryService.getSummaryErrorInquiries(pageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(inquiries));
    }

    /**
     * 해당 회원이 본인이 작성한 상품문의 리스트를 조회하기위한 메서드입니다.
     * 성공 시 200 반환
     *
     * @param pageable 상품문의 리스트가 담긴 페이징 정보
     * @param memberNo 상품문의를 조회할 회원 번호
     * @return the response entity
     */
    @MemberAuth
    @GetMapping("/token/inquiries/members/{memberNo}")
    public ResponseEntity<PageResponse<GetInquirySummaryMemberResponseDto>> memberInquiryList(
            Pageable pageable,
            @PathVariable("memberNo") Long memberNo) {
        Page<GetInquirySummaryMemberResponseDto> memberInquiries =
                inquiryService.getMemberInquiries(pageable, memberNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PageResponse<>(memberInquiries));
    }

    /**
     * 상품문의 단건 조회를 위한 메서드입니다.
     * 공개여부가 공개인 상품문의만 조회됩니다.
     * 성공 시 200 반환
     *
     * @return the response entity
     */
    @GetMapping("/api/inquiries/{inquiryNo}")
    public ResponseEntity<GetInquiryResponseDto> publicInquiryDetails(
            @PathVariable("inquiryNo") Long inquiryNo) {
        GetInquiryResponseDto inquiry = inquiryService.getInquiry(inquiryNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(inquiry);
    }


    /**
     * 상품문의 단건 조회를 위한 메서드입니다.
     * 공개여부가 비공개인 상품문의만 조회됩니다.
     * 성공 시 200 반환
     *
     * @param inquiryNo 조회할 상품문의 번호
     * @return the response entity
     */
    @MemberAndAuth
    @GetMapping("/token/inquiries/{inquiryNo}/members/{memberNo}")
    public ResponseEntity<GetInquiryResponseDto> privateInquiryDetails(
            @PathVariable("inquiryNo") Long inquiryNo) {
        GetInquiryResponseDto inquiry = inquiryService.getInquiry(inquiryNo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(inquiry);
    }

}
