package com.nhnacademy.bookpubshop.subscribe.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.service.SubscribeService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 구독관련 RestController 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService service;

    /**
     * 구독을생성하기위한 메서드입니다.
     * 성공시 201 이 반환됩니다.
     * 관리자만 접근가능합니다.
     *
     * @param dto 구독생성정보기입
     * @return the response entity
     */
    @AdminAuth
    @PostMapping("/token/subscribes")
    public ResponseEntity<Void> subscribeAdd(@Valid @RequestPart CreateSubscribeRequestDto dto,
                                             @RequestPart(value = "image") MultipartFile image) {
        service.createSubscribe(dto, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 구독리스트를 반환합니다.
     * 성공시 200 을 반환합니다.
     * 관리자만 접근가능합니다.
     *
     * @param pageable 페이징 정보
     * @return 구독들 반환
     */
    @GetMapping("/api/subscribes")
    public ResponseEntity<PageResponse<GetSubscribeResponseDto>> subscribeList(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PageResponse<>(service.getSubscribes(pageable)));
    }

    /**
     * 구독관련 삭제.
     * 성공시 200 반환.
     * 관리자만 접근가능.
     *
     * @param subscribeNo 구독번호 기입.
     * @param isDeleted   삭제여부 기입.
     */
    @AdminAuth
    @DeleteMapping("/token/subscribes/{subscribeNo}")
    public ResponseEntity<Void> subscribeDelete(@PathVariable("subscribeNo") Long subscribeNo,
                                                @RequestParam("isDeleted") boolean isDeleted) {
        service.deleteSubscribe(subscribeNo, isDeleted);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /**
     * 구독정보를 수정하기위해 사용되는 메서드입니다.
     * 관리자만 접근이 가능합니다.
     * 반환값으로는 201 이 반환됩니다.
     * 여담 : 수정인데 Post 를 한이유는 Multipart post 로만 전송해야한다.
     *
     * @param subscribeNo 구독번호
     * @param dto         the dto
     * @return the response entity
     */
    @AdminAuth
    @PostMapping(value = "/token/subscribes/{subscribeNo}",
            produces = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> subscribeModify(@PathVariable("subscribeNo") Long subscribeNo,
                                                @RequestPart("dto") ModifySubscribeRequestDto dto,
                                                @RequestPart("image") MultipartFile image) {
        service.modifySubscribe(dto, subscribeNo, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
