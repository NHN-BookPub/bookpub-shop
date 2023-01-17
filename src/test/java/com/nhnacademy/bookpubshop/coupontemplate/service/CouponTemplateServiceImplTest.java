//package com.nhnacademy.bookpubshop.coupontemplate.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
//import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepository;
//import com.nhnacademy.bookpubshop.couponstatecode.repository.CouponStateCodeRepository;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.dto.response.RestGetDetailCouponTemplateResponseDto;
//import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
//import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
//import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
//import com.nhnacademy.bookpubshop.coupontemplate.service.impl.CouponTemplateServiceImpl;
//import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepository;
//import com.nhnacademy.bookpubshop.file.repository.FileRepository;
//import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
//import com.nhnacademy.bookpubshop.utils.FileUtils;
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
///**
// * 쿠폰템플릿 서비스 테스트입니다.
// *
// * @author : 정유진
// * @since : 1.0
// **/
//@ExtendWith(SpringExtension.class)
//@Import(CouponTemplateServiceImpl.class)
//class CouponTemplateServiceImplTest {
//
//    @Autowired
//    CouponTemplateService couponTemplateService;
//
//    @MockBean
//    CouponTemplateRepository couponTemplateRepository;
//    @MockBean
//    CouponPolicyRepository couponPolicyRepository;
//    @MockBean
//    CouponTypeRepository couponTypeRepository;
//    @MockBean
//    ProductRepository productRepository;
//    @MockBean
//    CategoryRepository categoryRepository;
//    @MockBean
//    CouponStateCodeRepository couponStateCodeRepository;
//    @MockBean
//    FileUtils fileUtils;
//
//    CreateCouponTemplateRequestDto createRequestDto;
//    ModifyCouponTemplateRequestDto modifyRequestDto;
//    ArgumentCaptor<CouponTemplate> captor;
//
//    @BeforeEach
//    void setUp() {
//        createRequestDto = new CreateCouponTemplateRequestDto();
//        modifyRequestDto = new ModifyCouponTemplateRequestDto();
//
//        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
//        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
//        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
//        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
//        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
//        ReflectionTestUtils.setField(createRequestDto, "templateName", "test_templateName");
//        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.now());
//        ReflectionTestUtils.setField(createRequestDto, "issuedAt", LocalDateTime.now());
//        ReflectionTestUtils.setField(createRequestDto, "templateOverlapped", true);
//        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);
//
//        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
//        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
//        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
//        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
//        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
//        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "test_templateName");
//        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.now());
//        ReflectionTestUtils.setField(modifyRequestDto, "issuedAt", LocalDateTime.now());
//        ReflectionTestUtils.setField(modifyRequestDto, "templateOverlapped", true);
//        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);
//    }
//
//    @Test
//    @DisplayName("쿠폰템플릿 상세 정보 조회 성공 테스트")
//    void restGetDetailCouponTemplateTest_Success() throws IOException {
//        GetDetailCouponTemplateResponseDto dto =
//                new GetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.now(), LocalDateTime.now(), true, true);
//        RestGetDetailCouponTemplateResponseDto restDto =
//                new RestGetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_restImage", LocalDateTime.now(), LocalDateTime.now(), true, true);
//
//        when(couponTemplateRepository.findDetailByTemplateNo(anyLong()))
//                .thenReturn(Optional.of(dto));
//
//        when(fileUtils.loadFile(dto.getTemplateImage()))
//                .thenReturn(restDto.getTemplateName());
//
////        when(dto.transform(anyString()))
////                .thenReturn(restDto);
//
//        RestGetDetailCouponTemplateResponseDto result = couponTemplateService.getDetailCouponTemplate(dto.getTemplateNo());
//
//        assertThat(result.getTemplateNo()).isEqualTo(restDto.getTemplateNo());
//        assertThat(result.isPolicyFixed()).isEqualTo(restDto.isPolicyFixed());
//        assertThat(result.getPolicyPrice()).isEqualTo(restDto.getPolicyPrice());
//        assertThat(result.getPolicyMinimum()).isEqualTo(restDto.getPolicyMinimum());
//        assertThat(result.getMaxDiscount()).isEqualTo(restDto.getMaxDiscount());
//        assertThat(result.getMaxDiscount()).isEqualTo(restDto.getMaxDiscount());
//        assertThat(result.getTypeName()).isEqualTo(restDto.getTypeName());
//        assertThat(result.getCategoryName()).isEqualTo(restDto.getCategoryName());
//        assertThat(result.getCodeTarget()).isEqualTo(restDto.getCodeTarget());
//        assertThat(result.getTemplateName()).isEqualTo(restDto.getTemplateName());
//        //assertThat(result.getTemplateImage()).isEqualTo(restDto.getTemplateImage());
//        assertThat(result.getFinishedAt()).isEqualTo(restDto.getFinishedAt());
//        assertThat(result.getIssuedAt()).isEqualTo(restDto.getIssuedAt());
//        assertThat(result.isTemplateOverlapped()).isEqualTo(restDto.isTemplateOverlapped());
//        assertThat(result.isTemplateBundled()).isEqualTo(restDto.isTemplateBundled());
//
//        verify(couponTemplateRepository, times(1)).findDetailByTemplateNo(anyLong());
//    }
//
//    @Test
//    @DisplayName("쿠폰템플릿 상세 정보 조회 실패 테스트_쿠폰템플릿 번호를 찾을 수 없음")
//    void getDetailCouponTemplateTest_Fail_CouponTemplateNotFoundException() {
//        when(couponTemplateRepository.findDetailByTemplateNo(anyLong()))
//                .thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> couponTemplateService.getDetailCouponTemplate(anyLong()))
//                .isInstanceOf(CouponTemplateNotFoundException.class)
//                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
//
//        verify(couponTemplateRepository, times(1)).findDetailByTemplateNo(anyLong());
//    }
//
//    @Test
//    @DisplayName("쿠폰템플릿 상세 정보 리스트 조회 성공 테스트")
//    void getDetailCouponTemplatesTest_Success() {
//        GetDetailCouponTemplateResponseDto dto =
//                new GetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.now(), LocalDateTime.now(), true, true);
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<GetDetailCouponTemplateResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
//
//        when(couponTemplateRepository.findDetailAllBy(pageable))
//                .thenReturn(page);
//
//        Page<GetDetailCouponTemplateResponseDto> result = couponTemplateService.getDetailCouponTemplates(pageable);
//        List<GetDetailCouponTemplateResponseDto> content = result.getContent();
//
//        assertThat(content.get(0).getTemplateNo()).isEqualTo(dto.getTemplateNo());
//        assertThat(content.get(0).isPolicyFixed()).isEqualTo(dto.isPolicyFixed());
//        assertThat(content.get(0).getPolicyPrice()).isEqualTo(dto.getPolicyPrice());
//        assertThat(content.get(0).getPolicyMinimum()).isEqualTo(dto.getPolicyMinimum());
//        assertThat(content.get(0).getMaxDiscount()).isEqualTo(dto.getMaxDiscount());
//        assertThat(content.get(0).getMaxDiscount()).isEqualTo(dto.getMaxDiscount());
//        assertThat(content.get(0).getTypeName()).isEqualTo(dto.getTypeName());
//        assertThat(content.get(0).getCategoryName()).isEqualTo(dto.getCategoryName());
//        assertThat(content.get(0).getCodeTarget()).isEqualTo(dto.getCodeTarget());
//        assertThat(content.get(0).getTemplateName()).isEqualTo(dto.getTemplateName());
//        assertThat(content.get(0).getTemplateImage()).isEqualTo(dto.getTemplateImage());
//        assertThat(content.get(0).getFinishedAt()).isEqualTo(dto.getFinishedAt());
//        assertThat(content.get(0).getIssuedAt()).isEqualTo(dto.getIssuedAt());
//        assertThat(content.get(0).isTemplateOverlapped()).isEqualTo(dto.isTemplateOverlapped());
//        assertThat(content.get(0).isTemplateBundled()).isEqualTo(dto.isTemplateBundled());
//
//        verify(couponTemplateRepository, times(1)).findDetailAllBy(pageable);
//    }
//
//    @Test
//    @DisplayName("쿠폰템플릿 기본 정보 리스트 조회 성공 테스트")
//    void getCouponTemplatesTest_Success() throws IOException {
//        GetCouponTemplateResponseDto dto =
//                new GetCouponTemplateResponseDto(1L, "test_name", "test_image", LocalDateTime.now(), LocalDateTime.now());
//
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<GetCouponTemplateResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);
//
//        when(couponTemplateRepository.findAllBy(pageable))
//                .thenReturn(page);
//
//        Page<RestGetCouponTemplateResponseDto> result = couponTemplateService.getCouponTemplates(pageable);
//        List<RestGetCouponTemplateResponseDto> content = result.getContent();
//
//        assertThat(content.get(0).getTemplateName()).isEqualTo(dto.getTemplateName());
//        assertThat(content.get(0).getTemplateImage()).isEqualTo(dto.getTemplateImage());
//        assertThat(content.get(0).getIssuedAt()).isEqualTo(dto.getIssuedAt());
//        assertThat(content.get(0).getFinishedAt()).isEqualTo(dto.getFinishedAt());
//
//        verify(couponTemplateRepository, times(1)).findAllBy(pageable);
//
//    }
//
//    @Test
//    void createCouponTemplate() {
//    }
//
//    @Test
//    void modifyCouponTemplate() {
//    }
//}