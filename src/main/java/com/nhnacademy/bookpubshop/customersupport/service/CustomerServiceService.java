package com.nhnacademy.bookpubshop.customersupport.service;

import com.nhnacademy.bookpubshop.customersupport.dto.CreateCustomerServiceRequestDto;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 고객 서비스 서비스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface CustomerServiceService {
    /**
     * 고객서비스를 등록합니다.
     *
     * @param request 등록 정보 dto
     * @param image 고객서비스 이미지
     * @throws IOException 파일이 야기시킬 수 있는 예외
     */
    void createCustomerService(CreateCustomerServiceRequestDto request, MultipartFile image) throws IOException;

    /**
     * 모든 고객서비스들을 조회합니다.
     *
     * @param pageable 페이징.
     * @return 고객서비스들.
     */
    PageResponse<GetCustomerServiceListResponseDto> getCustomerServices(Pageable pageable);

    /**
     * 코드명으로 고객서비스를 조회합니다.
     *
     * @param codeName 코드명
     * @return 고객서비스 리스트
     */
    PageResponse<GetCustomerServiceListResponseDto> getCustomerServicesByCodeName(String codeName, Pageable pageable);

    /**
     * 카테고리로 고객서비스를 조회합니다.
     *
     * @param category 카테고리
     * @return 고객서비스 리스트
     */
    PageResponse<GetCustomerServiceListResponseDto> getCustomerServicesByCategory(String category, Pageable pageable);
}
