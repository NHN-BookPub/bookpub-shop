package com.nhnacademy.bookpubshop.order.service;

import com.nhnacademy.bookpubshop.order.dto.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * 멤버 서비스입니다.
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderService {
    /**
     * 주문 상세 정보를 조회합니다.
     *
     * @param orderNo 주문번호.
     * @return 주문상세 정보 반환.
     */
    GetOrderDetailResponseDto getOrderDetailById(Long orderNo);

    /**
     * 주문을 등록합니다.
     *
     * @param request dto 객체.
     * @param memberNo 멤버번호.
     */
    void createOrder(CreateOrderRequestDto request, Long memberNo);

    /**
     * 송장번호를 수정합니다.
     *
     * @param orderNo 주문번호.
     * @param invoiceNo 송장번호.
     */
    void modifyInvoiceNumber(Long orderNo, String invoiceNo);


    /**
     * 상태코드를 수정합니다.
     *
     * @param stateCode 상태코드명.
     * @param orderNo 주문번호.
     */
    void modifyStateCode(
            @StateCode(enumClass = OrderState.class) String stateCode,
            Long orderNo);

    /**
     * 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @return 모든 주문 반환.
     */
    PageResponse<GetOrderListForAdminResponseDto> getOrderList(Pageable pageable);

    /**
     * 멤버의 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @param memberNo 멤버번호.
     * @return 멤버의 모든 주문 반환.
     */
    PageResponse<GetOrderListResponseDto> getOrderListByUsers(Pageable pageable, Long memberNo);
}
