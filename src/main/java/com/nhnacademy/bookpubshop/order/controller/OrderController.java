package com.nhnacademy.bookpubshop.order.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAndAuth;
import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.service.OrderService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 컨트롤러 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    /**
     * 모든 주문을 반환합니다.
     *
     * @param pageable 페이징을 위해 받습니다.
     * @return 200, 모든 주문 반환.
     */
    @AdminAuth
    @GetMapping("/token/orders")
    public ResponseEntity<PageResponse<GetOrderListForAdminResponseDto>> getOrders(
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderList(pageable));
    }

    /**
     * 상태코드로 주문을 조회합니다.
     *
     * @param pageable 페이징
     * @param codeName 상태코드명(영어)
     * @return 주문목록
     */
    @AdminAuth
    @GetMapping("/token/orders/{codeName}")
    public ResponseEntity<PageResponse<GetOrderListForAdminResponseDto>> getOrdersByCodeName(
            Pageable pageable,
            @PathVariable String codeName) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderListByCodeName(pageable, codeName));
    }

    /**
     * 주문을 등록합니다.
     *
     * @param request 등록을 위한 Dto 객체를 받습니다.
     * @return 201 반환.
     */
    @PostMapping("/api/orders")
    public ResponseEntity<Long> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        log.warn("파싱 : {}", request.getCouponAmount().toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.createOrder(request));

    }

    /**
     * 송장번호만 수정합니다.
     *
     * @param orderNo 주문번호입니다.
     * @param no      수정할 송장번호.
     * @return 201 반환.
     */
    @PutMapping("/api/orders/{orderNo}/invoice")
    public ResponseEntity<Void> modifyInvoiceNo(@PathVariable Long orderNo,
                                                @RequestParam String no) {
        orderService.modifyInvoiceNumber(orderNo, no);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 상태코드만 수정합니다.
     *
     * @param orderNo 주문번호.
     * @param code    코드명.
     * @return 201 반환.
     */
    @PutMapping("/api/orders/{orderNo}/state")
    public ResponseEntity<Void> modifyStateCode(
            @PathVariable Long orderNo, @RequestParam String code) {
        orderService.modifyStateCode(code, orderNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 멤버 번호로 멤버의 모든 주문을 반환합니다.
     *
     * @param pageable 페이징을 위해 받습니다.
     * @return 200, 멤버의 모든 주문 반환.
     */
    @MemberAuth
    @GetMapping("/token/orders/member/{memberNo}")
    public ResponseEntity<PageResponse<GetOrderListResponseDto>> getOrdersByMember(
            Pageable pageable,
            @PathVariable("memberNo") Long memberNo) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderListByUsers(pageable, memberNo));
    }


    /**
     * 주문 상세 정보를 반환합니다. (주문 번호로 조회)
     *
     * @param orderNo 주문번호입니다.
     * @return 200, 주문상세정보 Dto 를 반환합니다.
     */
    @MemberAndAuth
    @GetMapping("/token/orders/{orderNo}/members/{memberNo}")
    public ResponseEntity<GetOrderDetailResponseDto> getOrderDetailByOrderNo(
            @PathVariable Long orderNo) {
        GetOrderDetailResponseDto response = orderService.getOrderDetailById(orderNo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 주문 한 정보를 확인시키는 dto를 반환합니다.
     *
     * @param orderNo 주문번호.
     * @return 주문정보.
     */
    @GetMapping("/api/orders/{orderNo}")
    public ResponseEntity<GetOrderConfirmResponseDto> getOrderConfirmInfo(
            @PathVariable Long orderNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderConfirmInfo(orderNo));
    }

    /**
     * 비회원 주문 상세 정보를 반환합니다. (주문 Id 조회)
     *
     * @param orderId 주문 Id 입니다.
     * @return 200, 주문상세정보 Dto 를 반환합니다.
     */
    @GetMapping("/api/orders/non/{orderId}")
    public ResponseEntity<GetOrderDetailResponseDto> getOrderDetailByOrderId(
            @PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderDetailByOrderId(orderId));
    }

    /**
     * 주문,결제 정보를 반환하는 api.
     *
     * @param orderId 주문아이디.
     * @return 주문, 결제 정보.
     */
    @GetMapping("/api/orders/payment/{orderId}")
    public ResponseEntity<GetOrderAndPaymentResponseDto> getOrderAndPaymentInfo(
            @PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderService.getOrderAndPaymentInfo(orderId));
    }

    /**
     * 주문상품 상태를 구매확정으로 만드는 메소드.
     *
     * @param memberNo       회원번호.
     * @param orderProductNo 주문상품번호.
     * @return 성공상태.
     */
    @MemberAuth
    @PutMapping("/token/orders/order-product/{orderProductNo}/member/{memberNo}")
    public ResponseEntity<Void> confirmOrderProduct(
            @PathVariable String memberNo, @PathVariable String orderProductNo) {
        orderService.confirmOrderProduct(orderProductNo, memberNo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    /**
     * 교환 수락 메소드.
     *
     * @param orderProductNo 주문상품번호.
     * @return 성공상태번호.
     */
    @AdminAuth
    @PostMapping("/token/orders/order-product/{orderProductNo}")
    public ResponseEntity<Void> confirmExchange(@PathVariable String orderProductNo) {
        orderService.confirmExchange(orderProductNo);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
