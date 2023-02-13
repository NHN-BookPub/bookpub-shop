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
@RequestMapping
public class CustomerServiceController {
    private final CustomerServiceService customerServiceService;

    @PostMapping("/token/services")
    @AdminAuth
    public ResponseEntity<Void> createCustomerService(
            @Valid @RequestPart CreateCustomerServiceRequestDto requestDto,
            @RequestPart(required = false) MultipartFile image) throws IOException {
        customerServiceService.createCustomerService(requestDto, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/token/services")
    @AdminAuth
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServices(Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServices(pageable));
    }

    @GetMapping("/api/services/{codeName}")
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServicesByCodeName(@PathVariable String codeName, Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServicesByCodeName(codeName, pageable));
    }

    @GetMapping("/api/services/category/{category}")
    public ResponseEntity<PageResponse<GetCustomerServiceListResponseDto>>
    getCustomerServicesByCategory(@PathVariable String category, Pageable pageable) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerServiceService.getCustomerServicesByCategory(category, pageable));
    }
}
