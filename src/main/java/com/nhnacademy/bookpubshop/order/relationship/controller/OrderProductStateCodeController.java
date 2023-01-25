package com.nhnacademy.bookpubshop.order.relationship.controller;

import com.nhnacademy.bookpubshop.order.relationship.dto.CreateOrderProductStateCodeRequestDto;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.service.OrderProductStateCodeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문상품상태코드 컨트롤러입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/state/orderproducts")
public class OrderProductStateCodeController {
    private final OrderProductStateCodeService orderProductStateCodeService;

    /**
     * 모든 상태코드를 반환합니다.
     *
     * @return 200, 모든 상태코드 반환.
     */
    @GetMapping
    public ResponseEntity<List<GetOrderProductStateCodeResponseDto>> getAllCodes() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderProductStateCodeService.getOrderProductStateCodes());
    }

    /**
     * 코드 번호로 상태코드를 조회합니다.
     *
     * @param codeNo 코드번호.
     * @return 200, 코드Dto 반환.
     */
    @GetMapping("/{codeNo}")
    public ResponseEntity<GetOrderProductStateCodeResponseDto> getStateCodeById(
            @PathVariable Integer codeNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderProductStateCodeService.getOrderProductStateCode(codeNo));
    }

    /**
     * 상태코드를 등록합니다.
     *
     * @param request 코드 생성을 위한 Dto.
     * @return 201 반환.
     */
    @PostMapping
    public ResponseEntity<Void> createStateCode(
            @RequestBody @Valid CreateOrderProductStateCodeRequestDto request) {
        orderProductStateCodeService.createOrderProductStateCode(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상태코드를 수정합니다.
     *
     * @param codeNo 코드번호.
     * @return 201 반환.
     */
    @PutMapping("/{codeNo}")
    public ResponseEntity<Void> modifyStateCode(
            @PathVariable Integer codeNo,
            @RequestParam boolean used) {
        orderProductStateCodeService.modifyUsedOrderProductStateCode(codeNo, used);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
