package com.nhnacademy.bookpubshop.customersupport.repository;

import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 고객서비스 레포지토리 커스텀입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@NoRepositoryBean
public interface CustomerServiceRepositoryCustom {

    /**
     * 전체 고객서비스를 조회합니다.
     *
     * @param pageable 페이징
     * @return 고객서비스
     */
    Page<GetCustomerServiceListResponseDto> getCustomerServices(Pageable pageable);

    /**
     * 코드명으로 고객서비스를 조회합니다.
     *
     * @param codeName 코드명
     * @return 고객서비스 리스트
     */
    Page<GetCustomerServiceListResponseDto> getCustomerServicesByCodeName(String codeName, Pageable pageable);

    /**
     * 카테고리로 고객서비스를 조회합니다.
     *
     * @param category 카테고리
     * @return 고객서비스 리스트
     */
    Page<GetCustomerServiceListResponseDto> getCustomerServicesByCategory(String category, Pageable pageable);

    /**
     * 서비스 단건 조회.
     *
     * @param serviceNo 서비스번호
     * @return 서비스 단건
     */
    Optional<GetCustomerServiceListResponseDto> findCustomerServiceByNo(Integer serviceNo);
}
