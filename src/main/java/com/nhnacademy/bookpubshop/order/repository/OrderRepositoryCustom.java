package com.nhnacademy.bookpubshop.order.repository;

import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderVerifyResponseDto;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.sales.dto.response.OrderCntResponseDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleDto;
import com.nhnacademy.bookpubshop.sales.dto.response.TotalSaleYearDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문레포지토리에서 querydsl을 사용하기 위한 custom 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderRepositoryCustom {
    /**
     * 주문의 상세 정보를 반환합니다.(주문번호로 조회)
     *
     * @param orderNo 주문번호.
     * @return 주문상세 정보를 반환.
     */
    Optional<GetOrderDetailResponseDto> getOrderDetailById(Long orderNo);

    /**
     * 주문의 상세 정보를 반환합니다.(주문 Id 조회)
     *
     * @param orderId 주문번호.
     * @return 주문상세 정보를 반환.
     */
    Optional<GetOrderDetailResponseDto> getOrderDetailByOrderId(String orderId);

    /**
     * 모든 주문을 반환합니다.
     *
     * @param pageable 페이징을 위해 받습니다.
     * @return 모든 주문을 반환.
     */
    Page<GetOrderListForAdminResponseDto> getOrderList(Pageable pageable);

    /**
     * 멤버의 모든 주문을 반환합니다.
     *
     * @param pageable 페이징.
     * @param memberNo 멤버 번호.
     * @return 멤버의 모든 주문 반환.
     */
    Page<GetOrderListResponseDto> getOrdersListByUser(Pageable pageable, Long memberNo);

    /**
     * 주문과 결제생성의 요청금액이 같은지 검사하기 위해 주문 테이블에 기입된 amount를 가져옵니다.
     *
     * @param orderId 주문아이디.
     * @return amount 정보가 담겨있는 dto.
     */
    Optional<GetOrderVerifyResponseDto> verifyPayment(String orderId);

    /**
     * order의 unique한 아이디로 주문을 가져옵니다.
     *
     * @param orderId 주문아이디.
     * @return amount 정보가 담겨있는 dto.
     */
    Optional<BookpubOrder> getOrderByOrderKey(String orderId);

    /**
     * 결제창에서 주문 정보를 최종적으로 확인하기 위한 메소드.
     *
     * @param orderNo 주문번호.
     * @return 주문정보.
     */
    Optional<GetOrderConfirmResponseDto> getOrderConfirmInfo(Long orderNo);

    /**
     * 최종 결제 성공 후 메인화면에서 띄워줄 정보.
     *
     * @param orderId 주문아이디.
     * @return 주문, 결제 정보.
     */
    Optional<GetOrderAndPaymentResponseDto> getOrderAndPayment(String orderId);

    /**
     * 주문 번호로 주문상품들을 가져오는 메소드.
     *
     * @param orderNo 주문번호.
     * @return 주문상품들.
     */
    List<OrderProduct> getOrderProductList(Long orderNo);

    /**
     * 매출값을 얻기위한 메서드입니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return 매출정보
     */
    List<TotalSaleDto> getTotalSale(LocalDateTime start, LocalDateTime end);

    /**
     * 월별 매출통계를 얻기위한 메서드입니다.
     *
     * @param start 시작일자
     * @param end   종료일자
     * @return 매출정보
     */
    List<TotalSaleYearDto> getTotalSaleMonth(LocalDateTime start, LocalDateTime end);

    /**
     * 시간대별 주문 현황을 보기위한 메서드입니다.
     *
     * @return 시간대별 주문현황반환
     */
    List<OrderCntResponseDto> getOrderTime();

    /**
     * 주문 상태에 따라 주문목록을 반환합니다.
     *
     * @param pageable 페이징
     * @param codeName 상태코드명
     * @return 상태별 주문목록
     */
    Page<GetOrderListForAdminResponseDto>
    getOrderListByCodeName(Pageable pageable, String codeName);
}
