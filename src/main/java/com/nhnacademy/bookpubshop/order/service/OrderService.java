package com.nhnacademy.bookpubshop.order.service;

import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.state.OrderState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.Map;
import org.springframework.data.domain.Pageable;

/**
 * 멤버 서비스입니다.
 *
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
     * 주문 상세 정보를 조회합니다.(주문Id 조회)
     *
     * @param orderId 주문 Id
     * @return 주문상세정보
     */
    GetOrderDetailResponseDto getOrderDetailByOrderId(String orderId);

    /**
     * 주문을 등록합니다.
     *
     * @param request dto 객체.
     */
    Long createOrder(CreateOrderRequestDto request);

    /**
     * 주문 상품을 등록합니다.
     *
     * @param request          dto객체.
     * @param order            주문
     * @param productCoupon    상품에 쓰인 쿠폰.
     * @param productPointSave 상품에 적립될 포인트.
     */
    void createOrderProduct(CreateOrderRequestDto request,
                            BookpubOrder order,
                            Map<Long, Long> productCoupon,
                            Map<Long, Long> productPointSave);

    /**
     * 송장번호를 수정합니다.
     *
     * @param orderNo   주문번호.
     * @param invoiceNo 송장번호.
     */
    void modifyInvoiceNumber(Long orderNo, String invoiceNo);


    /**
     * 상태코드를 수정합니다.
     *
     * @param stateCode 상태코드명.
     * @param orderNo   주문번호.
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

    /**
     * 주문, 결제 최종정보를 반환하는 메소드.
     *
     * @param orderId 주문아이디.
     * @return 주문, 결제 정보.
     */
    GetOrderAndPaymentResponseDto getOrderAndPaymentInfo(String orderId);

    /**
     * 결제 전 주문정보를 최종적으로 확인하기 위한 메소드.
     *
     * @param orderNo 주문번호.
     * @return 주문정보.
     */
    GetOrderConfirmResponseDto getOrderConfirmInfo(Long orderNo);

    /**
     * 주문상품의 상태를 구매확정으로 변경시켜주는 메소드.
     *
     * @param orderProductNo 주문상품번호.
     * @param memberNo       회원번호.
     */
    void confirmOrderProduct(String orderProductNo, String memberNo);

    /**
     * 주문상품의 상태를 교환완료로 변경시켜주는 메소드.
     * '
     * @param orderProductNo 주문상품번호.
     */
    void confirmExchange(String orderProductNo);

    /**
     * 주문 상태에 따라 주문목록을 반환합니다.
     *
     * @param pageable 페이징
     * @param codeName 상태코드명
     * @return 상태별 주문목록
     */
    PageResponse<GetOrderListForAdminResponseDto>
    getOrderListByCodeName(Pageable pageable, String codeName);
}
