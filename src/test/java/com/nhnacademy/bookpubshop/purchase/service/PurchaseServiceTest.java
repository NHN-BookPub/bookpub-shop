package com.nhnacademy.bookpubshop.purchase.service;

import static com.nhnacademy.bookpubshop.state.ProductTypeState.BEST_SELLER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.exception.NotFoundPurchasesException;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookpubshop.purchase.service.impl.PurchaseServiceImpl;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.nhnacademy.bookpubshop.wishlist.repository.WishlistRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * PurchaseServiceTest.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(PurchaseServiceImpl.class)
class PurchaseServiceTest {
    @MockBean
    ProductRepository productRepository;
    @MockBean
    PurchaseRepository purchaseRepository;
    @MockBean
    ProductSaleStateCodeRepository productSaleStateCodeRepository;
    @MockBean
    ProductTypeStateCodeRepository productTypeStateCodeRepository;
    @MockBean
    WishlistRepository wishlistRepository;
    @MockBean
    MemberRepository memberRepository;
    @Autowired
    PurchaseService purchaseService;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode typeStateCode;
    ProductSaleStateCode saleStateCode;
    Purchase purchase;
    ArgumentCaptor<Purchase> captor;
    Pageable pageable;
    GetPurchaseListResponseDto listResponse;
    CreatePurchaseRequestDto request;

    @BeforeEach
    void setUp() {
        productPolicy = new ProductPolicy(1, "method", true, 1);
        typeStateCode = new ProductTypeStateCode(1, BEST_SELLER.getName(), BEST_SELLER.isUsed(), "info");
        saleStateCode = new ProductSaleStateCode(1, ProductSaleState.SALE.name(), ProductSaleState.SALE.isUsed(), "info");

        product = new Product(1L,
                productPolicy,
                typeStateCode,
                saleStateCode,
                null,
                "1231231233",
                "test",
                "test_publisher",
                130,
                "test_description",
                8000L,
                10000L,
                20,
                0L,
                10,
                false,
                100,
                LocalDateTime.now(),
                false);

        purchase = new Purchase(
                1L,
                product,
                10000L,
                20);

        captor = ArgumentCaptor.forClass(Purchase.class);

        pageable = Pageable.ofSize(5);

        listResponse = new GetPurchaseListResponseDto(
                product.getProductNo(),
                product.getTitle(),
                purchase.getPurchaseNo(),
                purchase.getPurchaseAmount(),
                purchase.getPurchasePrice(),
                purchase.getCreatedAt());

        request = new CreatePurchaseRequestDto();

        ReflectionTestUtils.setField(request, "productNo", product.getProductNo());
        ReflectionTestUtils.setField(request, "purchasePrice", purchase.getPurchasePrice());
        ReflectionTestUtils.setField(request, "purchaseAmount", purchase.getPurchaseAmount());
        ReflectionTestUtils.setField(request, "productType", 1);
    }

    @Test
    @DisplayName("상품번호로 구매이력 조회 성공")
    void getPurchaseByProductNo() {
        List<GetPurchaseListResponseDto> response = new ArrayList<>();
        response.add(listResponse);

        Page<GetPurchaseListResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        when(purchaseRepository.findByProductNumberWithPage(product.getProductNo(), pageable))
                .thenReturn(page);

        assertThat(purchaseService.getPurchaseByProductNo(
                        product.getProductNo(), pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(product.getProductNo());
        assertThat(purchaseService.getPurchaseByProductNo(
                        product.getProductNo(), pageable)
                .getContent().get(0).getPurchasePrice())
                .isEqualTo(purchase.getPurchasePrice());
        assertThat(purchaseService.getPurchaseByProductNo(
                        product.getProductNo(), pageable)
                .getContent().get(0).getPurchaseNo())
                .isEqualTo(purchase.getPurchaseNo());
        assertThat(purchaseService.getPurchaseByProductNo(
                        product.getProductNo(), pageable)
                .getContent().get(0).getPurchaseAmount())
                .isEqualTo(purchase.getPurchaseAmount());
    }

    @Test
    @DisplayName("구매이력 등록 성공")
    void createPurchase() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(purchaseRepository.save(any()))
                .thenReturn(purchase);

        purchaseService.createPurchase(request);

        verify(purchaseRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("구매이력 등록 실패(없는 상품)")
    void createPurchaseFailed() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                purchaseService.createPurchase(request))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("구매이력 수정 성공")
    void modifyPurchase() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(purchaseRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(purchase));
        when(purchaseRepository.save(any()))
                .thenReturn(purchase);

        purchaseService.modifyPurchase(purchase.getPurchaseNo(), request);

        verify(purchaseRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("구매이력 수정 실패(없는 상품으로 변경)")
    void modifyPurchaseFailedNoProduct() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());
        when(purchaseRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(purchase));

        assertThatThrownBy(() ->
                purchaseService.modifyPurchase(purchase.getPurchaseNo(), request))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("구매이력 수정 실패(존재하지 않는 구매이력 번호)")
    void modifyPurchaseFailedNoPurchase() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.ofNullable(product));
        when(purchaseRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                purchaseService.modifyPurchase(purchase.getPurchaseNo(), request))
                .isInstanceOf(NotFoundPurchasesException.class);
    }

    @Test
    @DisplayName("최신순으로 구매이력 조회")
    void getPurchaseListDesc() {
        List<GetPurchaseListResponseDto> response = new ArrayList<>();
        response.add(listResponse);

        Page<GetPurchaseListResponseDto> page =
                PageableExecutionUtils.getPage(response, pageable, () -> 1L);

        when(purchaseRepository.getPurchaseListDesc(pageable))
                .thenReturn(page);

        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getProductNo())
                .isEqualTo(product.getProductNo());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchasePrice())
                .isEqualTo(purchase.getPurchasePrice());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchaseNo())
                .isEqualTo(purchase.getPurchaseNo());
        assertThat(purchaseService.getPurchaseListDesc(pageable)
                .getContent().get(0).getPurchaseAmount())
                .isEqualTo(purchase.getPurchaseAmount());
    }

    @Test
    @DisplayName("최신순으로 구매이력 조회 실패(이력 없음)")
    void getPurchaseListDescFailed() {
        Page<GetPurchaseListResponseDto> page =
                PageableExecutionUtils.getPage(Collections.EMPTY_LIST
                        , pageable, () -> 1L);

        when(purchaseRepository.getPurchaseListDesc(pageable))
                .thenReturn(page);

        assertThat(purchaseService.getPurchaseListDesc(pageable).getContent().isEmpty());
    }


    @Test
    @DisplayName("매입이력 등록시 상품 재고 증가")
    void createPurchaseMerged() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.of(product));
        when(productSaleStateCodeRepository.findByCodeCategory(ProductSaleState.SALE.getName()))
                .thenReturn(Optional.of(saleStateCode));
        when(productTypeStateCodeRepository.findById(anyInt()))
                .thenReturn(Optional.of(typeStateCode));
        when(purchaseRepository.save(any()))
                .thenReturn(purchase);
        when(productRepository.save(any()))
                .thenReturn(product);

        purchaseService.createPurchaseMerged(request);

        verify(productRepository, times(1))
                .findById(anyLong());
        verify(productTypeStateCodeRepository, times(1))
                .findById(anyInt());

    }

    @Test
    @DisplayName("매입이력 등록시 상품 재고 증가 실패(없는 상품)")
    void createPurchaseMergedFailed() {
        when(productRepository.findById(product.getProductNo()))
                .thenReturn(Optional.empty());
        when(purchaseRepository.save(any()))
                .thenReturn(purchase);

        assertThatThrownBy(() ->
                purchaseService.createPurchaseMerged(request))
                .isInstanceOf(ProductNotFoundException.class);
    }
}