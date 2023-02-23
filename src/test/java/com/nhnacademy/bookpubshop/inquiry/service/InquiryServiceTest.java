package com.nhnacademy.bookpubshop.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.repository.FileRepository;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.inquiry.dto.request.CreateInquiryRequestDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dummy.InquiryDummy;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquiry.exception.InquiryNotFoundException;
import com.nhnacademy.bookpubshop.inquiry.repository.InquiryRepository;
import com.nhnacademy.bookpubshop.inquiry.service.impl.InquiryServiceImpl;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.exception.InquiryStateCodeNotFoundException;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 상품문의 서비스 테스트.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(InquiryServiceImpl.class)
class InquiryServiceTest {
    @Autowired
    InquiryService inquiryService;
    @MockBean
    InquiryRepository inquiryRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    InquiryStateCodeRepository inquiryStateCodeRepository;
    @MockBean
    FileManagement fileManagement;
    @MockBean
    FileRepository fileRepository;

    CreateInquiryRequestDto createInquiryRequestDto;
    Member member;
    Inquiry inquiry;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    InquiryStateCode inquiryStateCode;
    File file;
    MockMultipartFile multipartFile;
    PageRequest pageRequest;
    GetInquirySummaryProductResponseDto getInquirySummaryProductResponseDto;
    GetInquirySummaryResponseDto getInquirySummaryResponseDto;
    GetInquirySummaryMemberResponseDto getInquirySummaryMemberResponseDto;
    GetInquiryResponseDto getInquiryResponseDto;
    Inquiry parent;

    @BeforeEach
    void setUp() {
        getInquiryResponseDto = InquiryDummy.getInquiryDummy();
        getInquirySummaryMemberResponseDto = InquiryDummy.getMemberDummy();
        getInquirySummaryResponseDto = InquiryDummy.getErrorInquiryDummy();
        getInquirySummaryProductResponseDto = InquiryDummy.getSummaryDummy();
        pageRequest = PageRequest.of(0, 10);
        String imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        inquiryStateCode = new InquiryStateCode(null, "name", true, "info");
        productPolicy = ProductPolicyDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
        member = MemberDummy.dummy2(new BookPubTier(null, 1, 1L, 1L));
        parent = InquiryDummy.dummy(null, member, product, inquiryStateCode);
        inquiry = InquiryDummy.dummy(parent, member, product, inquiryStateCode);
        createInquiryRequestDto = new CreateInquiryRequestDto();
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryParentNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "productNo", 1L);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryStateCodeNo", 1);
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryTitle", "title");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryContent", "content");
        ReflectionTestUtils.setField(createInquiryRequestDto, "inquiryDisplayed", true);
        multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/*", imageContent.getBytes());
        file = FileDummy.dummy(inquiry, null, null, null, null, null);
    }

    @DisplayName("회원이 해당상품문의를 작성할 수 있는지 확인.")
    @Test
    void verifyWritableInquiry() {
        when(inquiryRepository.existsPurchaseHistoryByMemberNo(anyLong(), anyLong()))
                .thenReturn(true);
        boolean result = inquiryService.verifyWritableInquiry(1L, 1L);

        assertThat(result).isTrue();
        verify(inquiryRepository, times(1)).existsPurchaseHistoryByMemberNo(1L, 1L);
    }

    @DisplayName("문의 생성 시 :문의를 찾지못했을경우 에러")
    @Test
    void createInquiryFail() {
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.createInquiry(1L, createInquiryRequestDto))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessageContaining(InquiryNotFoundException.MESSAGE);

    }

    @DisplayName("문의 생성 시 : 회원을 찾지못할 경우")
    @Test
    void createInquiryFail2() {
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(inquiry));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.createInquiry(1L, createInquiryRequestDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("문의 생성 시 : 상품을 찾지 못했을 때 나는 에러")
    @Test
    void createInquiryFail3() {
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(inquiry));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.createInquiry(1L, createInquiryRequestDto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @DisplayName("문의 생성 시 : InquiryStateCodeNotFoundException")
    @Test
    void createInquiryFail4() {
        ReflectionTestUtils.setField(inquiry,"inquiryImages",List.of(file));
        ReflectionTestUtils.setField(parent,"inquiryImages",List.of(file));
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(inquiry));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(inquiryStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.createInquiry(1L, createInquiryRequestDto))
                .isInstanceOf(InquiryStateCodeNotFoundException.class)
                .hasMessageContaining(InquiryStateCodeNotFoundException.MESSAGE);

    }

    @DisplayName("문의 생성")
    @Test
    void createInquirySuccessTest() {
        ReflectionTestUtils.setField(inquiry, "inquiryImages", List.of(file));
        ReflectionTestUtils.setField(parent, "inquiryImages", List.of(file));
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(parent));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(inquiryStateCodeRepository.findById(any()))
                .thenReturn(Optional.of(inquiryStateCode));
        when(inquiryRepository.save(any()))
                .thenReturn(inquiry);
        inquiryService.createInquiry(1L, createInquiryRequestDto);
    }

    @DisplayName("문의 의미지를 추가 테스트")
    @Test
    void addInquiryImage() throws IOException {
        when(fileManagement.saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString()))
                .thenReturn(file);

        String result = inquiryService.addInquiryImage(multipartFile);
        assertThat(result).isEqualTo(file.getFilePath());

        verify(fileManagement, times(1))
                .saveFile(any(), any(), any(), any(), any(), any(), any(), anyString(), anyString());
    }

    @DisplayName("삭제시 파일을 찾지못했을 경우")
    @Test
    void deleteInquiryFail() {
        ReflectionTestUtils.setField(inquiry, "inquiryImages", List.of(file));

        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.deleteInquiry(1L))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessageContaining(InquiryNotFoundException.MESSAGE);

    }

    @DisplayName("삭제시 파일을 찾지못했을 경우")
    @Test
    void deleteInquirySuccess() throws IOException {
        ReflectionTestUtils.setField(inquiry, "inquiryImages", List.of(file));

        when(inquiryRepository.findByParentInquiry_InquiryNo(any()))
                .thenReturn(List.of(inquiry));
        when(inquiryRepository.findById(anyLong()))
                .thenReturn(Optional.of(inquiry));
        doNothing().when(fileManagement)
                .deleteFile(anyString());
        doNothing().when(fileManagement)
                .deleteFile(anyString());
        doNothing().when(inquiryRepository)
                .delete(inquiry);

        inquiryService.deleteInquiry(1L);

        verify(inquiryRepository, times(1))
                .findById(1L);
        verify(fileManagement, times(2))
                .deleteFile(anyString());

    }

    @DisplayName("상품문의 답변을 삭제 문의찾기 실패")
    @Test
    void deleteAnswerInquiryFail() {
        when(inquiryRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.deleteInquiryAnswer(anyLong()))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessageContaining(InquiryNotFoundException.MESSAGE);
    }

    @DisplayName("상품문의 답변을 삭제 성공")
    @Test
    void deleteAnswerInquirySuccess() throws IOException {
        ReflectionTestUtils.setField(inquiry, "inquiryImages", List.of(file));

        when(inquiryRepository.findById(any()))
                .thenReturn(Optional.of(inquiry));
        doNothing().when(fileManagement)
                .deleteFile(anyString());
        doNothing().when(inquiryRepository)
                .delete(inquiry);

        inquiryService.deleteInquiryAnswer(1L);

        verify(inquiryRepository, times(1))
                .findById(anyLong());
        verify(fileManagement, times(1))
                .deleteFile(anyString());
        verify(inquiryRepository, times(1))
                .delete(any());

    }

    @DisplayName("수정 상품문의 찾지못해 실패")
    @Test
    void modifyCompleteInquiryFail() {
        when(inquiryRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> inquiryService.modifyCompleteInquiry(1L))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessageContaining(InquiryNotFoundException.MESSAGE);
    }

    @DisplayName("수정 상품문의 성공")
    @Test
    void modifyComplete() {
        when(inquiryRepository.findById(any()))
                .thenReturn(Optional.of(inquiry));

        inquiryService.modifyCompleteInquiry(1L);
        verify(inquiryRepository, times(1))
                .findById(any());
    }

    @DisplayName("상품문의 정보 조회")
    @Test
    void getSummaryInquiriesByProduct() {
        PageImpl<GetInquirySummaryProductResponseDto> page = new PageImpl<>(List.of(getInquirySummaryProductResponseDto));
        when(inquiryRepository.findSummaryInquiriesByProduct(any(), anyLong()))
                .thenReturn(page);

        Page<GetInquirySummaryProductResponseDto> result
                = inquiryService.getSummaryInquiriesByProduct(pageRequest, 1L);

        List<GetInquirySummaryProductResponseDto> content = result.getContent();
        assertThat(result).isNotEmpty();
        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getInquiryNo()).isEqualTo(getInquirySummaryProductResponseDto.getInquiryNo());
        assertThat(content.get(0).getProductNo()).isEqualTo(getInquirySummaryProductResponseDto.getProductNo());
        assertThat(content.get(0).getMemberNo()).isEqualTo(getInquirySummaryProductResponseDto.getMemberNo());
        assertThat(content.get(0).getInquiryStateCodeName()).isEqualTo(getInquirySummaryProductResponseDto.getInquiryStateCodeName());
        assertThat(content.get(0).getMemberNickname()).isEqualTo(getInquirySummaryProductResponseDto.getMemberNickname());
        assertThat(content.get(0).getInquiryTitle()).isEqualTo(getInquirySummaryProductResponseDto.getInquiryTitle());
        assertThat(content.get(0).isInquiryDisplayed()).isEqualTo(getInquirySummaryProductResponseDto.isInquiryDisplayed());
        assertThat(content.get(0).isInquiryAnswered()).isEqualTo(getInquirySummaryProductResponseDto.isInquiryAnswered());
        assertThat(content.get(0).getCreatedAt()).isEqualTo(getInquirySummaryProductResponseDto.getCreatedAt());

        verify(inquiryRepository, times(1)).findSummaryInquiriesByProduct(pageRequest, 1L);
    }

    @DisplayName("불량상품문의 성공")
    @Test
    void getSummaryErrorInquiries() {
        PageImpl<GetInquirySummaryResponseDto> page = new PageImpl<>(List.of(getInquirySummaryResponseDto));
        when(inquiryRepository.findSummaryErrorInquiries(any()))
                .thenReturn(page);

        Page<GetInquirySummaryResponseDto> pageResponse = inquiryService.getSummaryErrorInquiries(pageRequest);
        List<GetInquirySummaryResponseDto> result = pageResponse.getContent();
        assertThat(pageResponse).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getInquiryNo()).isEqualTo(getInquirySummaryResponseDto.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(getInquirySummaryResponseDto.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(getInquirySummaryResponseDto.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(getInquirySummaryResponseDto.getInquiryStateCodeName());
        assertThat(result.get(0).getMemberNickname()).isEqualTo(getInquirySummaryResponseDto.getMemberNickname());
        assertThat(result.get(0).getProductTitle()).isEqualTo(getInquirySummaryResponseDto.getProductTitle());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(getInquirySummaryResponseDto.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(getInquirySummaryResponseDto.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(getInquirySummaryResponseDto.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(getInquirySummaryResponseDto.getCreatedAt());

        verify(inquiryRepository, times(1))
                .findSummaryErrorInquiries(pageRequest);
    }

    @DisplayName("문의들을 조회하는 메서드테스트")
    @Test
    void getSummaryInquiries() {
        PageImpl<GetInquirySummaryResponseDto> page = new PageImpl<>(List.of(getInquirySummaryResponseDto));
        when(inquiryRepository.findSummaryInquiries(any(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(page);
        Page<GetInquirySummaryResponseDto> pageResponse = inquiryService.getSummaryInquiries(pageRequest, "key",
                "anyString()", "anyString()", "anyString()");
        List<GetInquirySummaryResponseDto> result = pageResponse.getContent();

        assertThat(pageResponse).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getInquiryNo()).isEqualTo(getInquirySummaryResponseDto.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(getInquirySummaryResponseDto.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(getInquirySummaryResponseDto.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(getInquirySummaryResponseDto.getInquiryStateCodeName());
        assertThat(result.get(0).getMemberNickname()).isEqualTo(getInquirySummaryResponseDto.getMemberNickname());
        assertThat(result.get(0).getProductTitle()).isEqualTo(getInquirySummaryResponseDto.getProductTitle());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(getInquirySummaryResponseDto.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(getInquirySummaryResponseDto.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(getInquirySummaryResponseDto.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(getInquirySummaryResponseDto.getCreatedAt());

        verify(inquiryRepository, times(1))
                .findSummaryInquiries(pageRequest, "key",
                        "anyString()", "anyString()", "anyString()");
    }

    @DisplayName("회원의 문의 조회")
    @Test
    void getMemberInquiries() {
        PageImpl<GetInquirySummaryMemberResponseDto> page = new PageImpl<>(List.of(getInquirySummaryMemberResponseDto));
        when(inquiryRepository.findMemberInquiries(any(), anyLong()))
                .thenReturn(page);

        Page<GetInquirySummaryMemberResponseDto> pageResponse = inquiryService.getMemberInquiries(pageRequest, 1L);
        List<GetInquirySummaryMemberResponseDto> result = pageResponse.getContent();

        assertThat(pageResponse).isNotEmpty();
        assertThat(result).isNotEmpty();

        assertThat(result.get(0).getInquiryNo()).isEqualTo(getInquirySummaryMemberResponseDto.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(getInquirySummaryMemberResponseDto.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(getInquirySummaryMemberResponseDto.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(getInquirySummaryMemberResponseDto.getInquiryStateCodeName());
        assertThat(result.get(0).getProductTitle()).isEqualTo(getInquirySummaryMemberResponseDto.getProductTitle());
        assertThat(result.get(0).getProductImagePath()).isEqualTo(getInquirySummaryMemberResponseDto.getProductImagePath());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(getInquirySummaryMemberResponseDto.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(getInquirySummaryMemberResponseDto.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(getInquirySummaryMemberResponseDto.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(getInquirySummaryMemberResponseDto.getCreatedAt());

        verify(inquiryRepository, times(1))
                .findMemberInquiries(pageRequest, 1L);

    }

    @DisplayName("문의 번호로문의 조회 : 실패할경우")
    @Test
    void getInquiryFail() {
        assertThatThrownBy(() -> inquiryService.getInquiry(1L))
                .isInstanceOf(InquiryNotFoundException.class)
                .hasMessageContaining(InquiryNotFoundException.MESSAGE);

    }

    @DisplayName("문의 번호로문의 조회")
    @Test
    void getInquiry() {
        when(inquiryRepository.findInquiry(anyLong()))
                .thenReturn(Optional.of(getInquiryResponseDto));

        GetInquiryResponseDto result = inquiryService.getInquiry(1L);

        assertThat(result.getMemberNickname()).isEqualTo(getInquiryResponseDto.getMemberNickname());
        assertThat(result.getInquiryNo()).isEqualTo(getInquiryResponseDto.getInquiryNo());
        assertThat(result.getProductNo()).isEqualTo(getInquiryResponseDto.getProductNo());
        assertThat(result.getMemberNo()).isEqualTo(getInquiryResponseDto.getMemberNo());
        assertThat(result.getInquiryStateCodeName()).isEqualTo(getInquiryResponseDto.getInquiryStateCodeName());
        assertThat(result.getProductTitle()).isEqualTo(getInquiryResponseDto.getProductTitle());
        assertThat(result.getInquiryTitle()).isEqualTo(getInquiryResponseDto.getInquiryTitle());
        assertThat(result.getInquiryContent()).isEqualTo(getInquiryResponseDto.getInquiryContent());
        assertThat(result.isInquiryDisplayed()).isEqualTo(getInquiryResponseDto.isInquiryDisplayed());
        assertThat(result.isInquiryAnswered()).isEqualTo(getInquiryResponseDto.isInquiryAnswered());

        verify(inquiryRepository, times(1))
                .findInquiry(1L);
    }
}