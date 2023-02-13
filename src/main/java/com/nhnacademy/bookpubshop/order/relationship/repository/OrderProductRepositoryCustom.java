package com.nhnacademy.bookpubshop.order.relationship.repository;

import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문상품레포지토리 커스텀.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderProductRepositoryCustom {
    /**
     * 주문번호로 하위에 있는 모든 주문상품들을 가져오는 메소드.
     *
     * @param orderNo 주문번호.
     * @return 주문상품리스트.
     */
    List<OrderProduct> getOrderProductList(Long orderNo);

    /**
     * 주문상품 번호로 주문상품을 가져오는 메소드.
     *
     * @param orderProductNo 주문상품번호.
     * @return 주문상품.
     */
    Optional<OrderProduct> getOrderProduct(Long orderProductNo);
}
