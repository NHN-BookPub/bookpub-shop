package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.custom.OrderProductStateCodeRepositoryCustom;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.state.anno.StateCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주문상품상태코드 레포지토리.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface OrderProductStateCodeRepository
        extends JpaRepository<OrderProductStateCode, Integer>, OrderProductStateCodeRepositoryCustom {
    /**
     * 코드명으로 코드를 조회합니다.
     *
     * @param codeName 코드명입니다.
     * @return
     */
    Optional<OrderProductStateCode> findByCodeName(
            @StateCode(enumClass = OrderProductState.class)
            String codeName);
}