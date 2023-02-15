package com.nhnacademy.bookpubshop.customersupport.controller;

import com.nhnacademy.bookpubshop.annotation.AdminAuth;
import com.nhnacademy.bookpubshop.customersupport.dto.CreateCustomerServiceRequestDto;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.customersupport.service.CustomerServiceService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 고객 서비스 컨트롤러 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@RestController
@RequiredArgsConstructor
public class CustomerServiceController {
    private final CustomerServiceService customerServiceService;

    /**
     * 고객서비스를 생성합니다.
     *
     * @param requestDto 생성시 필요한 dto
     * @param image 이미지
     * @return 성공시 201
     * @throws IOException 파일 등록시 발생할 수 있는 예외
     */
    @PostMapping("/token/services")
    @AdminAuth
    public ResponseEntity<Void> createCustomerService(
            @Valid @RequestPart CreateCustomerServiceRequestDto requestDto,
            @RequestPart(required = false) MultipartFile image) throws IOException {
        customerServiceService.createCustomerService(requestDto, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * 전체 고객서비스를 조회합니다.
     *
     * @param pageable 페이징
     * @return 성공시 200, 고객서비스
     */
    @GetMapping("/token/services")
    @AdminAuth
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServices(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServices(pageable));
    }

    /**
     * 코드명으로 고객서비스를 조회합니다.
     *
     * @param codeName 코드명
     * @param pageable 페이징
     * @return 성공시 200
     */
    @GetMapping("/api/services/{codeName}")
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServicesByCodeName(@PathVariable String codeName,
                                  @PageableDefault Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServicesByCodeName(codeName, pageable));
    }

    /**
     * 카테고리로 고객서비스를 조회합니다.
     *
     * @param category 카테고리
     * @param pageable 페이징
     * @return 성공시 200
     */
    @GetMapping("/api/services/category/{category}")
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServicesByCategory(@PathVariable String category,
                                  @PageableDefault Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServicesByCategory(category, pageable));
    }

    /**
     * 서비스 단건 조회.
     *
     * @param serviceNo 서비스번호
     * @return 서비스 단건
     */
    @GetMapping("/api/service/{serviceNo}")
    public ResponseEntity<GetCustomerServiceListResponseDto> getCustomerService(@PathVariable Integer serviceNo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.findCustomerServiceByNo(serviceNo));
    }

    /**
     * 고객서비스 삭제
     *
     * @param serviceNo 서비스 번호
     * @return 성공시 201
     */
    @DeleteMapping("/token/services/{serviceNo}")
    @AdminAuth
    public ResponseEntity<Void> deleteCustomerService(@PathVariable Integer serviceNo) {
        customerServiceService.deleteCustomerServiceByNo(serviceNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
