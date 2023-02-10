package com.nhnacademy.bookpubshop.coupontemplate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponpolicy.exception.CouponPolicyNotFoundException;
import com.nhnacademy.bookpubshop.couponpolicy.repository.CouponPolicyRepository;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.couponstatecode.exception.CouponStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.couponstatecode.repository.CouponStateCodeRepository;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.CreateCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.request.ModifyCouponTemplateRequestDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dto.response.GetDetailCouponTemplateResponseDto;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontemplate.entity.CouponTemplate;
import com.nhnacademy.bookpubshop.coupontemplate.exception.CouponTemplateNotFoundException;
import com.nhnacademy.bookpubshop.coupontemplate.repository.CouponTemplateRepository;
import com.nhnacademy.bookpubshop.coupontemplate.service.impl.CouponTemplateServiceImpl;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.coupontype.exception.CouponTypeNotFoundException;
import com.nhnacademy.bookpubshop.coupontype.repository.CouponTypeRepository;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 쿠폰템플릿 서비스 테스트입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(CouponTemplateServiceImpl.class)
class CouponTemplateServiceImplTest {

    @Autowired
    CouponTemplateService couponTemplateService;

    @MockBean
    CouponTemplateRepository couponTemplateRepository;
    @MockBean
    CouponPolicyRepository couponPolicyRepository;
    @MockBean
    CouponTypeRepository couponTypeRepository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    CategoryRepository categoryRepository;
    @MockBean
    CouponStateCodeRepository couponStateCodeRepository;
    @MockBean
    FileManagement fileManagement;

    CouponType couponType;
    CouponPolicy couponPolicy;
    CouponStateCode couponStateCode;
    CouponTemplate couponTemplate;
    Product product;
    ProductPolicy productPolicy;
    ProductSaleStateCode productSaleStateCode;
    ProductTypeStateCode productTypeStateCode;
    Category category;
    CreateCouponTemplateRequestDto createRequestDto;
    ModifyCouponTemplateRequestDto modifyRequestDto;
    ArgumentCaptor<CouponTemplate> captor;

    @BeforeEach
    void setUp() {
        captor = ArgumentCaptor.forClass(CouponTemplate.class);

        couponType = CouponTypeDummy.dummy();
        couponPolicy = CouponPolicyDummy.dummy();
        couponStateCode = CouponStateCodeDummy.dummy();
        productPolicy = ProductPolicyDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        productTypeStateCode = new ProductTypeStateCode(1, "test_name", true, "test_info");
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        category = CategoryDummy.dummy();
        couponTemplate = CouponTemplateDummy.dummy(couponPolicy, couponType, product, category, couponStateCode);


        createRequestDto = new CreateCouponTemplateRequestDto();
        modifyRequestDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(createRequestDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createRequestDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createRequestDto, "templateBundled", true);

        ReflectionTestUtils.setField(modifyRequestDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyRequestDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyRequestDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyRequestDto, "finishedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(modifyRequestDto, "templateBundled", true);

    }

    @Test
    @DisplayName("쿠폰템플릿 상세 정보 조회 성공 테스트_이미지가 있는 경우")
    void restGetDetailCouponTemplateTestWithImage_Success() {
        GetDetailCouponTemplateResponseDto dto =
                new GetDetailCouponTemplateResponseDto(1L, true, 1L, 1L, 1L, "test_typeName", "test_title", "test_categoryName", "test_target", "test_name", "test_image", LocalDateTime.of(1, 1, 1, 1, 1), true);

        when(couponTemplateRepository.existsById(anyLong())).thenReturn(true);

        when(couponTemplateRepository.findDetailByTemplateNo(anyLong()))
                .thenReturn(Optional.of(dto));

        GetDetailCouponTemplateResponseDto result = couponTemplateService.getDetailCouponTemplate(dto.getTemplateNo());

        assertThat(result.getTemplateNo()).isEqualTo(dto.getTemplateNo());
        assertThat(result.isPolicyFixed()).isEqualTo(dto.isPolicyFixed());
        assertThat(result.getPolicyPrice()).isEqualTo(dto.getPolicyPrice());
        assertThat(result.getPolicyMinimum()).isEqualTo(dto.getPolicyMinimum());
        assertThat(result.getMaxDiscount()).isEqualTo(dto.getMaxDiscount());
        assertThat(result.getMaxDiscount()).isEqualTo(dto.getMaxDiscount());
        assertThat(result.getTypeName()).isEqualTo(dto.getTypeName());
        assertThat(result.getCategoryName()).isEqualTo(dto.getCategoryName());
        assertThat(result.getCodeTarget()).isEqualTo(dto.getCodeTarget());
        assertThat(result.getTemplateName()).isEqualTo(dto.getTemplateName());
        assertThat(result.getFinishedAt()).isEqualTo(dto.getFinishedAt());
        assertThat(result.isTemplateBundled()).isEqualTo(dto.isTemplateBundled());

        verify(couponTemplateRepository, times(1)).existsById(anyLong());
        verify(couponTemplateRepository, times(1)).findDetailByTemplateNo(anyLong());
    }

    @Test
    @DisplayName("쿠폰템플릿 상세 정보 조회 실패 테스트_쿠폰템플릿 번호를 찾을 수 없음")
    void getDetailCouponTemplateTest_Fail_CouponTemplateNotFoundException() {
        when(couponTemplateRepository.findDetailByTemplateNo(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponTemplateService.getDetailCouponTemplate(1L))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰템플릿 명을 통해 쿠폰템플릿을 가져오는 테스트")
    void getCouponTemplateByCouponTemplateName() {
        when(couponTemplateRepository.findDetailByTemplateName(anyString()))
                .thenReturn(Optional.of(couponTemplate));

        CouponTemplate template
                = couponTemplateService.getCouponTemplateByName("회원가입 축하 쿠폰");

        assertThat(template.getTemplateNo()).isEqualTo(couponTemplate.getTemplateNo());
        assertThat(template.getTemplateName()).isEqualTo(couponTemplate.getTemplateName());
        assertThat(template.getCouponPolicy()).isEqualTo(couponTemplate.getCouponPolicy());
        assertThat(template.getFinishedAt()).isEqualTo(couponTemplate.getFinishedAt());
        assertThat(template.getCouponType()).isEqualTo(couponTemplate.getCouponType());
        assertThat(template.getFile()).isEqualTo(couponTemplate.getFile());

    }

    @Test
    @DisplayName("쿠폰템플릿 명을 통해 쿠폰템플릿을 가져오는데 실패하는 테스트")
    void getCouponTemplateByCouponTemplateName_fail() {
        when(couponTemplateRepository.findDetailByTemplateName(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                couponTemplateService.getCouponTemplateByName("회원가입 축하 쿠폰"))
                .isInstanceOf(CouponTemplateNotFoundException.class);
    }

    @Test
    @DisplayName("쿠폰템플릿 기본 정보 리스트 조회 성공 테스트")
    void getCouponTemplatesTest_Success() throws IOException {
        GetCouponTemplateResponseDto dto =
                new GetCouponTemplateResponseDto(1L, "test_name", "test_image", LocalDateTime.now());
        GetCouponTemplateResponseDto dto_twice =
                new GetCouponTemplateResponseDto(1L, "test_name", null, LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10);

        Page<GetCouponTemplateResponseDto> page = new PageImpl<>(List.of(dto, dto_twice), pageable, 1);

        when(couponTemplateRepository.findAllBy(pageable))
                .thenReturn(page);

        Page<GetCouponTemplateResponseDto> result = couponTemplateService.getCouponTemplates(pageable);
        List<GetCouponTemplateResponseDto> content = result.getContent();

        assertThat(content.get(0).getTemplateName()).isEqualTo(dto.getTemplateName());
        assertThat(content.get(0).getTemplateImage()).isEqualTo(dto.getTemplateImage());
        assertThat(content.get(0).getFinishedAt()).isEqualTo(dto.getFinishedAt());
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 성공 테스트")
    void createCouponTemplateTest_Success() throws IOException {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", null);
        ReflectionTestUtils.setField(createDto, "categoryNo", null);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        File file = new File(null, null, null, null, null,
                null, null, "test_category", "test_path", "test_extension", "test_origin", "test_saved");
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(file);
        when(couponTypeRepository.findById(anyLong())).
                thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponStateCode));

        couponTemplateService.createCouponTemplate(createDto, multipartFile);

        verify(couponTemplateRepository, times(1))
                .save(captor.capture());
        CouponTemplate result = captor.getValue();

        assertThat(result.getCouponPolicy()).isEqualTo(couponPolicy);
        assertThat(result.getCouponType()).isEqualTo(couponType);
        assertThat(result.getCouponStateCode()).isEqualTo(couponStateCode);
        assertThat(result.getProduct()).isNull();
        assertThat(result.getCategory()).isNull();
        assertThat(result.getTemplateName()).isEqualTo(createDto.getTemplateName());
        assertThat(result.getFinishedAt()).isEqualTo(createDto.getFinishedAt());
        assertThat(result.isTemplateBundled()).isEqualTo(createDto.isTemplateBundled());

        verify(fileManagement, times(1)).saveFile(any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(couponTypeRepository, times(1)).findById(anyLong());
        verify(couponPolicyRepository, times(1)).findById(anyInt());
        verify(couponStateCodeRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 실패 테스트_상품번호가 없는 경우")
    void createCouponTemplateTest_Fail_ProductNotFound() {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", 1L);
        ReflectionTestUtils.setField(createDto, "categoryNo", null);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(createDto, multipartFile))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 실패 테스트_카테고리번호가 없는 경우")
    void createCouponTemplateTest_Fail_CategoryNotFound() {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", null);
        ReflectionTestUtils.setField(createDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(createDto, multipartFile))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(CategoryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 실패 테스트_쿠폰정책번호가 없는 경우")
    void createCouponTemplateTest_Fail_CouponPolicyNotFound() {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", null);
        ReflectionTestUtils.setField(createDto, "categoryNo", 1);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(createDto, multipartFile))
                .isInstanceOf(CouponPolicyNotFoundException.class)
                .hasMessageContaining(CouponPolicyNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 실패 테스트_쿠폰유형번호가 없는 경우")
    void createCouponTemplateTest_Fail_CouponTypeNotFound() {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", null);
        ReflectionTestUtils.setField(createDto, "categoryNo", null);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(createDto, multipartFile))
                .isInstanceOf(CouponTypeNotFoundException.class)
                .hasMessageContaining(CouponTypeNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 생성 실패 테스트_쿠폰상태코드가 없는 경우")
    void createCouponTemplateTest_Fail_CouponStateCodeNotFound() {
        CreateCouponTemplateRequestDto createDto = new CreateCouponTemplateRequestDto();

        ReflectionTestUtils.setField(createDto, "policyNo", 1);
        ReflectionTestUtils.setField(createDto, "typeNo", 1L);
        ReflectionTestUtils.setField(createDto, "productNo", null);
        ReflectionTestUtils.setField(createDto, "categoryNo", null);
        ReflectionTestUtils.setField(createDto, "codeNo", 1);
        ReflectionTestUtils.setField(createDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(createDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(createDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));

        assertThatThrownBy(() -> couponTemplateService.createCouponTemplate(createDto, multipartFile))
                .isInstanceOf(CouponStateCodeNotFoundException.class)
                .hasMessageContaining(CouponStateCodeNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 성공 테스트_파일 없는 경우")
    void modifyCouponTemplate_Success() throws IOException {

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponTypeRepository.findById(anyLong())).
                thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponPolicy));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));
        when(couponStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponStateCode));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(null);

        couponTemplateService.modifyCouponTemplate(1L, modifyRequestDto, null);

        verify(couponTemplateRepository, times(1)).findById(1L);
        verify(couponTypeRepository, times(1)).findById(1L);
        verify(couponPolicyRepository, times(1)).findById(1);
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1);
        verify(couponStateCodeRepository, times(1)).findById(1);
        verify(fileManagement, times(1)).saveFile(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 성공 테스트_파일 있는 경우")
    void modifyCouponTemplateWithFile_Success() throws IOException {
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile file = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());
        File storeFile = new File(null, null, null, null, null,
                null, null, "test_category", "test_path", "test_extention", "test_origin", "test_saved");

        couponTemplate.setFile(storeFile);
        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponTypeRepository.findById(anyLong())).
                thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponPolicy));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));
        when(couponStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(couponStateCode));
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), any())).thenReturn(storeFile);
        doNothing().when(fileManagement).deleteFile(anyString());

        couponTemplateService.modifyCouponTemplate(1L, modifyRequestDto, file);

        verify(couponTemplateRepository, times(1)).findById(1L);
        verify(couponTypeRepository, times(1)).findById(1L);
        verify(couponPolicyRepository, times(1)).findById(1);
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1);
        verify(couponStateCodeRepository, times(1)).findById(1);
        verify(fileManagement, times(1)).deleteFile(storeFile.getFilePath());
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 실패 테스트_상품번호가 없는 경우")
    void modifyCouponTemplateTest_Fail_ProductNotFound() {
        ModifyCouponTemplateRequestDto modifyDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "productNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.modifyCouponTemplate(1L, modifyDto, multipartFile))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 실패 테스트_카테고리번호가 없는 경우")
    void modifyCouponTemplateTest_Fail_CategoryNotFound() {
        ModifyCouponTemplateRequestDto modifyDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "productNo", null);
        ReflectionTestUtils.setField(modifyDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.modifyCouponTemplate(1L, modifyDto, multipartFile))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining(CategoryNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 실패 테스트_쿠폰정책번호가 없는 경우")
    void modifyCouponTemplateTest_Fail_CouponPolicyNotFound() {
        ModifyCouponTemplateRequestDto modifyDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "productNo", null);
        ReflectionTestUtils.setField(modifyDto, "categoryNo", 1);
        ReflectionTestUtils.setField(modifyDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.modifyCouponTemplate(1L, modifyDto, multipartFile))
                .isInstanceOf(CouponPolicyNotFoundException.class)
                .hasMessageContaining(CouponPolicyNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 실패 테스트_쿠폰유형번호가 없는 경우")
    void modifyCouponTemplateTest_Fail_CouponTypeNotFound() {
        ModifyCouponTemplateRequestDto modifyDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "productNo", null);
        ReflectionTestUtils.setField(modifyDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.of(couponTemplate));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.modifyCouponTemplate(1L, modifyDto, multipartFile))
                .isInstanceOf(CouponTypeNotFoundException.class)
                .hasMessageContaining(CouponTypeNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 실패 테스트_쿠폰템플릿 번호가 없는 경우")
    void modifyCouponTemplateTest_Fail_CouponTemplateNoNotFound() {
        ModifyCouponTemplateRequestDto modifyDto = new ModifyCouponTemplateRequestDto();

        ReflectionTestUtils.setField(modifyDto, "policyNo", 1);
        ReflectionTestUtils.setField(modifyDto, "typeNo", 1L);
        ReflectionTestUtils.setField(modifyDto, "productNo", null);
        ReflectionTestUtils.setField(modifyDto, "categoryNo", null);
        ReflectionTestUtils.setField(modifyDto, "codeNo", 1);
        ReflectionTestUtils.setField(modifyDto, "templateName", "test_templateName");
        ReflectionTestUtils.setField(modifyDto, "finishedAt", LocalDateTime.of(1, 1, 1, 1, 1));
        ReflectionTestUtils.setField(modifyDto, "templateBundled", true);

        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        MultipartFile multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());

        when(couponTemplateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(couponTypeRepository.findById(anyLong())).thenReturn(Optional.of(couponType));
        when(couponPolicyRepository.findById(anyInt())).thenReturn(Optional.of(couponPolicy));
        when(couponStateCodeRepository.findById(anyInt())).thenReturn(Optional.of(couponStateCode));

        assertThatThrownBy(() -> couponTemplateService.modifyCouponTemplate(1L, modifyDto, multipartFile))
                .isInstanceOf(CouponTemplateNotFoundException.class)
                .hasMessageContaining(CouponTemplateNotFoundException.MESSAGE);
    }
}