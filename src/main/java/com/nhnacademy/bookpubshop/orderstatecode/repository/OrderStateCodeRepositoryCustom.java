package com.nhnacademy.bookpubshop.orderstatecode.repository;

import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 주문상태코드 레포지토리에서 query Dsl을 사용하기 위한 custom 클래스.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface OrderStateCodeRepositoryCustom {
    /**
     * 상태코드 번호로 조회합니다.
     *
     * @param codeNo 코드번호.
     * @return 코드 반환.
     */
    Optional<GetOrderStateCodeResponseDto> findStateCodeByNo(Integer codeNo);

    /**
     * 전체 상태 코드를 반환합니다.
     *
     * @return 상태코드 리스트.
     */
    List<GetOrderStateCodeResponseDto> findStateCodes();
}
