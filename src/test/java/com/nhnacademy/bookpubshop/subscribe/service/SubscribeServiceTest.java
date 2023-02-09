package com.nhnacademy.bookpubshop.subscribe.service;

import static com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeProductRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeProductListDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import com.nhnacademy.bookpubshop.subscribe.exception.SubscribeNotFoundException;
import com.nhnacademy.bookpubshop.subscribe.repository.SubscribeRepository;
import com.nhnacademy.bookpubshop.subscribe.service.impl.SubscribeServiceImpl;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 구독 서비스 테스트
 *
 * @author : 유호철
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(SubscribeServiceImpl.class)
class SubscribeServiceTest {
    @Autowired
    SubscribeService service;

    @MockBean
    SubscribeRepository repository;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    FileManagement fileManagement;

    Subscribe subscribe;
    Product product;
    CreateSubscribeRequestDto createSubscribeRequestDto;
    GetSubscribeResponseDto getSubscribeResponseDto;
    ModifySubscribeRequestDto modifySubscribeRequestDto;
    GetSubscribeDetailResponseDto getSubscribeDetailResponseDto;
    GetSubscribeProductListDto getSubscribeProductListDto;
    CreateSubscribeProductRequestDto createSubscribeProductRequestDto;
    ArgumentCaptor<Subscribe> captor;
    File file;

    @BeforeEach
    void setUp() {
        product = ProductDummy.dummy(ProductPolicyDummy.dummy(), ProductTypeStateCodeDummy.dummy(), ProductSaleStateCodeDummy.dummy());
        subscribe = dummy();
        getSubscribeProductListDto = listDto();
        createSubscribeProductRequestDto = productListDummy();
        getSubscribeDetailResponseDto = detailDummy();
        modifySubscribeRequestDto = modifyDummy();
        getSubscribeResponseDto = responseDummy();
        createSubscribeRequestDto = createDummy();
        captor = ArgumentCaptor.forClass(Subscribe.class);
        file = FileDummy.dummy(null, null
                , null, null, null, subscribe);
    }

    @DisplayName("구독생성 테스트 입니다.")
    @Test
    void createSubscribe() {
        when(repository.save(any()))
                .thenReturn(subscribe);

        service.createSubscribe(createSubscribeRequestDto, null);

        verify(repository, times(1))
                .save(captor.capture());
        Subscribe value = captor.getValue();
        assertThat(value.getSubscribeName()).isEqualTo(createSubscribeRequestDto.getName());
        assertThat(value.getSubscribePrice()).isEqualTo(createSubscribeRequestDto.getPrice());
        assertThat(value.getSalesPrice()).isEqualTo(createSubscribeRequestDto.getSalePrice());
        assertThat(value.getSalesRate()).isEqualTo(createSubscribeRequestDto.getSalesRate());
        assertThat(value.isSubscribeRenewed()).isEqualTo(createSubscribeRequestDto.isRenewed());

    }

    @DisplayName("구독을 찾을수 없을경우 발생하는 에러")
    @Test
    void deleteSubscribeFail() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteSubscribe(1L, true))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);

    }

    @DisplayName("구독삭제")
    @Test
    void deleteSubscribe() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));

        service.deleteSubscribe(1L, true);

        verify(repository, times(1))
                .findById(1L);
    }

    @DisplayName("구독정보들을 반환하기위한 메서드입니다.")
    @Test
    void getSubscribes() {
        PageImpl<GetSubscribeResponseDto> result = new PageImpl<>(List.of(getSubscribeResponseDto));
        PageRequest page = PageRequest.of(0, 10);
        when(repository.getSubscribes(any(Pageable.class)))
                .thenReturn(result);

        Page<GetSubscribeResponseDto> response = service.getSubscribes(page);

        assertThat(response.getContent().get(0).getSubscribeNo()).isEqualTo(getSubscribeResponseDto.getSubscribeNo());
        assertThat(response.getContent().get(0).getSubscribeName()).isEqualTo(getSubscribeResponseDto.getSubscribeName());
        assertThat(response.getContent().get(0).getPrice()).isEqualTo(getSubscribeResponseDto.getPrice());
        assertThat(response.getContent().get(0).getSalePrice()).isEqualTo(getSubscribeResponseDto.getSalePrice());
        assertThat(response.getContent().get(0).getSalesRate()).isEqualTo(getSubscribeResponseDto.getSalesRate());
        assertThat(response.getContent().get(0).getViewCnt()).isEqualTo(getSubscribeResponseDto.getViewCnt());
        assertThat(response.getContent().get(0).isDeleted()).isEqualTo(getSubscribeResponseDto.isDeleted());
        assertThat(response.getContent().get(0).isRenewed()).isEqualTo(getSubscribeResponseDto.isRenewed());

        verify(repository, times(1))
                .getSubscribes(page);
    }

    @DisplayName("구독 정보를 찾지 못할경우 Test")
    @Test
    void modifySubscribe() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.modifySubscribe(modifySubscribeRequestDto, 1L, any()))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);

    }

    @DisplayName("구독 정보 수정 성공 테스트")
    @Test
    void modifySubscribeSuccess() {
        ReflectionTestUtils.setField(subscribe, "file", file);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));
        service.modifySubscribe(modifySubscribeRequestDto, 1L, any());

        verify(repository, times(1))
                .findById(1L);
    }

    @DisplayName("구독상세정보 조회 실패 테스트")
    @Test
    void getSubscribeDetailFail() {
        when(repository.getSubscribeDetail(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSubscribeDetail(1L))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);
    }

    @DisplayName("구독상세정보 조회 성공 테스트")
    @Test
    void getSubscribeDetailSuccess() {
        when(repository.getSubscribeDetail(anyLong()))
                .thenReturn(Optional.of(getSubscribeDetailResponseDto));

        GetSubscribeDetailResponseDto subscribeDetail = service.getSubscribeDetail(anyLong());
        assertThat(subscribeDetail.getSubscribeNo()).isEqualTo(getSubscribeDetailResponseDto.getSubscribeNo());
        assertThat(subscribeDetail.getSubscribeName()).isEqualTo(getSubscribeDetailResponseDto.getSubscribeName());
        assertThat(subscribeDetail.getImagePath()).isEqualTo(getSubscribeDetailResponseDto.getImagePath());
        assertThat(subscribeDetail.getPrice()).isEqualTo(getSubscribeDetailResponseDto.getPrice());
        assertThat(subscribeDetail.getSalePrice()).isEqualTo(getSubscribeDetailResponseDto.getSalePrice());
        assertThat(subscribeDetail.getSalesRate()).isEqualTo(getSubscribeDetailResponseDto.getSalesRate());
        assertThat(subscribeDetail.getViewCnt()).isEqualTo(getSubscribeDetailResponseDto.getViewCnt());
        assertThat(subscribeDetail.isDeleted()).isEqualTo(getSubscribeDetailResponseDto.isDeleted());
        assertThat(subscribeDetail.isRenewed()).isEqualTo(getSubscribeDetailResponseDto.isRenewed());
        assertThat(subscribeDetail.getProductLists().get(0).getProductNo()).isEqualTo(getSubscribeProductListDto.getProductNo());
        assertThat(subscribeDetail.getProductLists().get(0).getTitle()).isEqualTo(getSubscribeProductListDto.getTitle());
        assertThat(subscribeDetail.getProductLists().get(0).getFilePath()).isEqualTo(getSubscribeProductListDto.getFilePath());

        verify(repository, times(1))
                .getSubscribeDetail(anyLong());
    }

    @DisplayName("구독연관 상품 추가 상품을 찾지못했을때 에러")
    @Test
    void addRelationTestFail() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addRelationProducts(1L, createSubscribeProductRequestDto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @DisplayName("구독연관 상품 추가 상품을 찾지못했을때 에러")
    @Test
    void addRelationTestSuccess() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        service.addRelationProducts(1L, createSubscribeProductRequestDto);

        verify(repository, times(1))
                .findById(1L);
        verify(productRepository, times(1))
                .findById(1L);
    }

    @DisplayName("구독 갱신수정 구독찾기 실패 테스트")
    @Test
    void modifyRenewedFail() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.modifySubscribeRenewed(1L, true))
                .isInstanceOf(SubscribeNotFoundException.class)
                .hasMessageContaining(SubscribeNotFoundException.MESSAGE);
    }

    @DisplayName("구독 갱신수정 구독찾기 성공 테스트")
    @Test
    void modifyRenewedSuccess() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(subscribe));
        service.modifySubscribeRenewed(1L, true);

        verify(repository, times(1))
                .findById(anyLong());
    }
}