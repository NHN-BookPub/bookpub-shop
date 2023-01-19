package com.nhnacademy.bookpubshop.order.relationship.service;

import com.nhnacademy.bookpubshop.order.relationship.dto.CreateOrderProductStateCodeRequestDto;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import java.util.List;

/**
 * 주문상품상태코드 서비스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderProductStateCodeService {
    /**
     * 주문상품상태코드를 등록합니다.
     *
     * @param request 등록을 위한 Dto.
     */
    void createOrderProductStateCode(CreateOrderProductStateCodeRequestDto request);

    /**
     * 사용여부를 수정합니다.
     *
     * @param codeNo 코드번호.
     */
    void modifyUsedOrderProductStateCode(Integer codeNo, boolean used);

    /**
     * 코드 번호로 조회합니다.
     *
     * @param codeNo 코드번호.
     * @return 주문상품상태코드 단건 반환.
     */
    GetOrderProductStateCodeResponseDto getOrderProductStateCode(Integer codeNo);

    /**
     * 전체 코드 번호를 반환합니다.
     *
     * @return 코드번호 리스트.
     */
    List<GetOrderProductStateCodeResponseDto> getOrderProductStateCodes();
}
