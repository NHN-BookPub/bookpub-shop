package com.nhnacademy.bookpubshop.orderstatecode.controller;

import com.nhnacademy.bookpubshop.orderstatecode.dto.CreateOrderStateCodeRequestDto;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.service.OrderStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문상태코드 컨트롤러 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequestMapping("/api/state/orderState")
@RequiredArgsConstructor
public class OrderStateCodeController {
    private final OrderStateCodeService orderStateCodeService;

    /**
     * 전체 주문상태코드를 반환합니다.
     *
     * @return 200, 전체 주문상태코드.
     */
    @GetMapping
    public ResponseEntity<List<GetOrderStateCodeResponseDto>> getOrderStateCodes() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderStateCodeService.getOrderStateCodes());
    }

    /**
     * 번호로 상태코드를 조회합니다.
     *
     * @param codeNo 코드 번호입니다.
     * @return 200, 단일 상태코드를 반환합니다.
     */
    @GetMapping("/{codeNo}")
    public ResponseEntity<GetOrderStateCodeResponseDto> getOrderStateCodeByNo(
            @PathVariable Integer codeNo
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderStateCodeService.getOrderStateCodeById(codeNo));
    }

    /**
     * 주문상태코드를 등록합니다.
     *
     * @param request Dto.
     * @return 201 반환.
     */
    @PostMapping
    public ResponseEntity<Void> createOrderStateCode(
            @RequestBody CreateOrderStateCodeRequestDto request) {
        orderStateCodeService.createPricePolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 사용여부를 수정합니다.
     *
     * @param codeNo 코드번호입니다.
     * @return 201 반환.
     */
    @PutMapping("/{codeNo}")
    public ResponseEntity<Void> modifyCodeUsed(
            @PathVariable Integer codeNo
    ) {
        orderStateCodeService.modifyOrderStateCodeUsed(codeNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
