package com.nhnacademy.bookpubshop.personalinquiryanswer.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request.CreatePersonalInquiryAnswerRequestDto;
import com.nhnacademy.bookpubshop.personalinquiryanswer.service.PersonalInquiryAnswerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1대1문의답변을 다루기 위한 컨트롤러입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class PersonalInquiryAnswerController {
    private final PersonalInquiryAnswerService personalInquiryAnswerService;

    /**
     * 1대1문의답변을 생성하기 위한 메서드입니다.
     *
     * @param createDto 1대1문의답변 생성 시 필요한 정보를 담은 dto
     */
    @AdminAuth
    @PostMapping("/token/personal-inquiry-answers")
    public ResponseEntity<Void> personalInquiryAnswerAdd(
            @Valid @RequestBody CreatePersonalInquiryAnswerRequestDto createDto) {
        personalInquiryAnswerService.createPersonalInquiryAnswer(createDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 1대1문의답변을 삭제하기 위한 메서드입니다.
     *
     * @param answerNo 삭제할 1대1문의답변 번호
     */
    @AdminAuth
    @DeleteMapping("/token/personal-inquiry-answers/{answerNo}")
    public ResponseEntity<Void> personalInquiryAnswerDelete(
            @PathVariable("answerNo") Long answerNo) {
        personalInquiryAnswerService.deletePersonalInquiryAnswer(answerNo);

        return ResponseEntity.ok()
                .build();
    }
}
