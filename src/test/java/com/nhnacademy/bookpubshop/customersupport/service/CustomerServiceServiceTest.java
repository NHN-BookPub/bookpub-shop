package com.nhnacademy.bookpubshop.customersupport.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.customersupport.dto.CreateCustomerServiceRequestDto;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.customersupport.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.customersupport.exception.CustomerServiceNotFoundException;
import com.nhnacademy.bookpubshop.customersupport.repository.CustomerServiceRepository;
import com.nhnacademy.bookpubshop.customersupport.service.impl.CustomerServiceServiceImpl;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.servicecode.repository.CustomerServiceStateCodeRepository;
import com.nhnacademy.bookpubshop.state.CustomerServiceCategory;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 고객서비스 서비스 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
class CustomerServiceServiceTest {
    CustomerServiceService customerServiceService;
    @MockBean
    CustomerServiceStateCodeRepository customerServiceStateCodeRepository;
    @MockBean
    CustomerServiceRepository customerServiceRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    TierRepository tierRepository;
    @MockBean
    FileManagement fileManagement;

    CustomerService customerService;
    CustomerServiceStateCode customerServiceStateCode;
    Member member;
    BookPubTier tier;
    MultipartFile multipartFile;
    CreateCustomerServiceRequestDto requestDto;
    GetCustomerServiceListResponseDto responseDto;
    Pageable pageable;
    Page<GetCustomerServiceListResponseDto> responseDtoPage;

    @BeforeEach
    void setUp() {
         customerServiceService = new CustomerServiceServiceImpl(
                 customerServiceRepository,
                 customerServiceStateCodeRepository,
                 memberRepository,
                 fileManagement);

         tier = TierDummy.dummy();
         customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
         member = MemberDummy.dummy(tier);

        customerService = CustomerServiceDummy.dummy(
                 customerServiceStateCode, member, "faqUsing");
        ReflectionTestUtils.setField(customerService, "serviceNo", 1);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        multipartFile = new MockMultipartFile(
                "image",
                "imageName.jpeg",
                "image/*",
                imageContent.getBytes());

        requestDto = new CreateCustomerServiceRequestDto();
        ReflectionTestUtils.setField(requestDto,
                "customerServiceStateCode",
                customerService.getCustomerServiceStateCode().getServiceCodeName());
        ReflectionTestUtils.setField(requestDto, "memberNo",
                1L);
        ReflectionTestUtils.setField(requestDto, "serviceCategory",
                customerService.getServiceCategory());
        ReflectionTestUtils.setField(requestDto, "serviceTitle",
                customerService.getServiceTitle());
        ReflectionTestUtils.setField(requestDto, "serviceContent",
                customerService.getServiceContent());

        responseDto = new GetCustomerServiceListResponseDto(
                1,
                customerServiceStateCode.getServiceCodeName(),
                customerService.getMember().getMemberId(),
                multipartFile.getOriginalFilename(),
                customerService.getServiceCategory(),
                customerService.getServiceTitle(),
                customerService.getServiceContent(),
                customerService.getCreatedAt()
        );

        pageable = Pageable.ofSize(10);

        responseDtoPage = PageableExecutionUtils.getPage(List.of(responseDto), pageable, () -> 1L);
    }

    @Test
    @DisplayName("고객서비스 생성 성공")
    void createCustomerService() throws IOException {
        when(customerServiceStateCodeRepository
                .getCustomerServiceStateCodeByName(any()))
                .thenReturn(Optional.of(customerServiceStateCode));
        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(member));

        customerServiceService.createCustomerService(requestDto, multipartFile);

        verify(customerServiceRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("고객서비스 조회 성공")
    void getCustomerServices() {
        when(customerServiceRepository.getCustomerServices(pageable))
                .thenReturn(responseDtoPage);

        assertThat(customerServiceService.getCustomerServices(pageable)
                .getContent().get(0).getCustomerServiceNo())
                .isEqualTo(customerService.getServiceNo());
        assertThat(customerServiceService.getCustomerServices(pageable)
                .getContent().get(0).getServiceTitle())
                .isEqualTo(customerService.getServiceTitle());
        assertThat(customerServiceService.getCustomerServices(pageable)
                .getContent().get(0).getServiceContent())
                .isEqualTo(customerService.getServiceContent());
        assertThat(customerServiceService.getCustomerServices(pageable)
                .getContent().get(0).getCustomerServiceStateCode())
                .isEqualTo(customerService.getCustomerServiceStateCode().getServiceCodeName());
        assertThat(customerServiceService.getCustomerServices(pageable)
                .getContent().get(0).getMemberId())
                .isEqualTo(customerService.getMember().getMemberId());
    }

    @Test
    @DisplayName("고객서비스 코드명으로 조회 성공")
    void getCustomerServicesByCodeName() {
        when(customerServiceRepository.getCustomerServicesByCodeName(
                customerService.getCustomerServiceStateCode().getServiceCodeName(),
                pageable))
                .thenReturn(responseDtoPage);

        assertThat(customerServiceService.getCustomerServicesByCodeName(
                customerServiceStateCode.getServiceCodeName(), pageable)
                .getContent().get(0).getCustomerServiceNo())
                .isEqualTo(customerService.getServiceNo());
        assertThat(customerServiceService.getCustomerServicesByCodeName(
                        customerServiceStateCode.getServiceCodeName(), pageable)
                .getContent().get(0).getServiceTitle())
                .isEqualTo(customerService.getServiceTitle());
        assertThat(customerServiceService.getCustomerServicesByCodeName(
                        customerServiceStateCode.getServiceCodeName(), pageable)
                .getContent().get(0).getServiceContent())
                .isEqualTo(customerService.getServiceContent());
        assertThat(customerServiceService.getCustomerServicesByCodeName(
                        customerServiceStateCode.getServiceCodeName(), pageable)
                .getContent().get(0).getCustomerServiceStateCode())
                .isEqualTo(customerService.getCustomerServiceStateCode().getServiceCodeName());
        assertThat(customerServiceService.getCustomerServicesByCodeName(
                        customerServiceStateCode.getServiceCodeName(), pageable)
                .getContent().get(0).getMemberId())
                .isEqualTo(customerService.getMember().getMemberId());
    }

    @Test
    @DisplayName("고객서비스 카테고리로 조회 성공")
    void getCustomerServicesByCategory() {
        when(customerServiceRepository.getCustomerServicesByCategory(
                CustomerServiceCategory.FAQ_USING.getName(),
                pageable))
                .thenReturn(responseDtoPage);

        assertThat(customerServiceService.getCustomerServicesByCategory(
                        customerService.getServiceCategory(), pageable)
                .getContent().get(0).getCustomerServiceNo())
                .isEqualTo(customerService.getServiceNo());
        assertThat(customerServiceService.getCustomerServicesByCategory(
                        customerService.getServiceCategory(), pageable)
                .getContent().get(0).getServiceTitle())
                .isEqualTo(customerService.getServiceTitle());
        assertThat(customerServiceService.getCustomerServicesByCategory(
                        customerService.getServiceCategory(), pageable)
                .getContent().get(0).getServiceContent())
                .isEqualTo(customerService.getServiceContent());
        assertThat(customerServiceService.getCustomerServicesByCategory(
                        customerService.getServiceCategory(), pageable)
                .getContent().get(0).getCustomerServiceStateCode())
                .isEqualTo(customerService.getCustomerServiceStateCode().getServiceCodeName());
        assertThat(customerServiceService.getCustomerServicesByCategory(
                        customerService.getServiceCategory(), pageable)
                .getContent().get(0).getMemberId())
                .isEqualTo(customerService.getMember().getMemberId());
    }

    @Test
    @DisplayName("고객서비스 번호로 단건 조회 성공")
    void findCustomerServiceByNo() {
        when(customerServiceRepository.findCustomerServiceByNo(
                customerService.getServiceNo()))
                .thenReturn(Optional.ofNullable(responseDto));

        assertThat(customerServiceService.findCustomerServiceByNo(
                        customerService.getServiceNo())
                .getCustomerServiceNo())
                .isEqualTo(customerService.getServiceNo());
        assertThat(customerServiceService.findCustomerServiceByNo(
                        customerService.getServiceNo())
                .getServiceTitle())
                .isEqualTo(customerService.getServiceTitle());
        assertThat(customerServiceService.findCustomerServiceByNo(
                        customerService.getServiceNo())
                .getServiceContent())
                .isEqualTo(customerService.getServiceContent());
        assertThat(customerServiceService.findCustomerServiceByNo(
                        customerService.getServiceNo())
                .getCustomerServiceStateCode())
                .isEqualTo(customerService.getCustomerServiceStateCode().getServiceCodeName());
        assertThat(customerServiceService.findCustomerServiceByNo(
                        customerService.getServiceNo())
                .getMemberId())
                .isEqualTo(customerService.getMember().getMemberId());
    }

    @Test
    @DisplayName("고객서비스 삭제 성공")
    void deleteCustomerServiceByNo() {
        when(customerServiceRepository.findById(customerService.getServiceNo()))
                .thenReturn(Optional.of(customerService));

        customerServiceService.deleteCustomerServiceByNo(customerService.getServiceNo());

        verify(customerServiceRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("고객서비스 삭제 실패")
    void deleteCustomerServiceByNoFailed() {
        when(customerServiceRepository.findById(customerService.getServiceNo()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerServiceService
                .deleteCustomerServiceByNo(customerService.getServiceNo()))
                .isInstanceOf(CustomerServiceNotFoundException.class);
    }
}