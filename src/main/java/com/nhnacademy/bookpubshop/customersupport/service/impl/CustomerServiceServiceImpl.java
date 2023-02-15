package com.nhnacademy.bookpubshop.customersupport.service.impl;

import com.nhnacademy.bookpubshop.customersupport.dto.CreateCustomerServiceRequestDto;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.customersupport.exception.CustomerServiceNotFoundException;
import com.nhnacademy.bookpubshop.customersupport.repository.CustomerServiceRepository;
import com.nhnacademy.bookpubshop.customersupport.service.CustomerServiceService;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.servicecode.repository.CustomerServiceStateCodeRepository;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 고객 서비스 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class CustomerServiceServiceImpl implements CustomerServiceService {
    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceStateCodeRepository customerServiceStateCodeRepository;
    private final MemberRepository memberRepository;
    private final FileManagement fileManagement;

    /**
     * {@inheritDoc}
     *
     * @throws IOException 파일을 등록할 때 발생할 수 있는 예외
     */
    @Override
    public void createCustomerService(CreateCustomerServiceRequestDto request, MultipartFile image) throws IOException {
        CustomerServiceStateCode customerServiceStateCode =
                customerServiceStateCodeRepository
                        .getCustomerServiceStateCodeByName(request.getCustomerServiceStateCode())
                        .orElseThrow(NotFoundStateCodeException::new);

        Member member = memberRepository.findById(request.getMemberNo())
                .orElseThrow(MemberNotFoundException::new);

        CustomerService customerService = customerServiceRepository.save(
                new CustomerService(
                        null,
                        customerServiceStateCode,
                        member,
                        request.getServiceCategory(),
                        request.getServiceTitle(),
                        request.getServiceContent()));

        fileManagement.saveFile(
                null,
                null,
                null,
                null,
                customerService,
                null,
                image,
                FileCategory.SERVICE.getCategory(),
                FileCategory.SERVICE.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetCustomerServiceListResponseDto> getCustomerServices(Pageable pageable) {
        return new PageResponse<>(customerServiceRepository.getCustomerServices(pageable));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetCustomerServiceListResponseDto> getCustomerServicesByCodeName(String codeName, Pageable pageable) {
        return new PageResponse<>(customerServiceRepository.getCustomerServicesByCodeName(codeName, pageable));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageResponse<GetCustomerServiceListResponseDto> getCustomerServicesByCategory(String category, Pageable pageable) {
        return new PageResponse<>(customerServiceRepository.getCustomerServicesByCategory(category, pageable));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetCustomerServiceListResponseDto findCustomerServiceByNo(Integer serviceNo) {
        return customerServiceRepository.findCustomerServiceByNo(serviceNo)
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCustomerServiceByNo(Integer serviceNo) {
        CustomerService customerService = customerServiceRepository.findById(serviceNo)
                .orElseThrow(() -> new CustomerServiceNotFoundException(serviceNo));

        customerServiceRepository.delete(customerService);
    }
}
