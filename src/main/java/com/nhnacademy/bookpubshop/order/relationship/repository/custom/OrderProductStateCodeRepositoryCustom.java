package com.nhnacademy.bookpubshop.order.relationship.repository.custom;

import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문상품상태코드 레포지토리 Custom.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderProductStateCodeRepositoryCustom {
    /**
     * 코드번호로 조회합니다.
     *
     * @param id 코드번호.
     * @return 코드 반환 Dto.
     */
    Optional<GetOrderProductStateCodeResponseDto> findCodeById(Integer id);

    /**
     * 모든 코드를 조회합니다.
     *
     * @return 모든 코드 반환.
     */
    List<GetOrderProductStateCodeResponseDto> findCodeAll();
}
