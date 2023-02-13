package com.nhnacademy.bookpubshop.purchase.service.impl;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.exception.NotFoundProductTypeException;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.exception.NotFoundPurchasesException;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import com.nhnacademy.bookpubshop.state.ProductSaleState;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetAppliedMemberResponseDto;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import com.nhnacademy.bookpubshop.wishlist.exception.WishlistNotFoundException;
import com.nhnacademy.bookpubshop.wishlist.repository.WishlistRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구현 서비스 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final ProductSaleStateCodeRepository productSaleStateCodeRepository;
    private final ProductTypeStateCodeRepository productTypeStateCodeRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<GetPurchaseListResponseDto> getPurchaseByProductNo(
            Long productNo, Pageable pageable) {
        Page<GetPurchaseListResponseDto> response =
                purchaseRepository.findByProductNumberWithPage(productNo, pageable);

        return new PageResponse<>(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createPurchase(CreatePurchaseRequestDto request) {
        Product product = productRepository
                .findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        purchaseRepository.save(request.toEntity(product));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyPurchase(Long purchaseId, CreatePurchaseRequestDto request) {
        Purchase purchase = purchaseRepository
                .findById(purchaseId)
                .orElseThrow(NotFoundPurchasesException::new);

        Product product = productRepository
                .findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        purchaseRepository.save(
                new Purchase(purchase.getPurchaseNo(),
                        product,
                        request.getPurchasePrice(),
                        request.getPurchaseAmount()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable) {
        return purchaseRepository.getPurchaseListDesc(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public List<GetAppliedMemberResponseDto> createPurchaseMerged(CreatePurchaseRequestDto request) {
        Product product = productRepository.findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);
        ProductTypeStateCode productTypeStateCode = productTypeStateCodeRepository.findById(request.getProductType())
                .orElseThrow(NotFoundProductTypeException::new);

        purchaseRepository.save(request.toEntity(product));
        product.plusStock(request.getPurchaseAmount());
        product.modifyProductType(productTypeStateCode);

        List<GetAppliedMemberResponseDto> wishlistAppliedMembers = new ArrayList<>();

        if (isSoldOutState(product)) {
            wishlistAppliedMembers = wishlistRepository.findWishlistAppliedMembers(product.getProductNo());
            for (GetAppliedMemberResponseDto wishlistAppliedMember : wishlistAppliedMembers) {
                Member wishMember = memberRepository.findById(wishlistAppliedMember.getMemberNo())
                        .orElseThrow(MemberNotFoundException::new);
                Product wishProduct = productRepository.findById(wishlistAppliedMember.getProductNo())
                        .orElseThrow(ProductNotFoundException::new);

                Wishlist wishlist = wishlistRepository.findById(new Wishlist.Pk(wishMember.getMemberNo(), wishProduct.getProductNo()))
                        .orElseThrow(WishlistNotFoundException::new);
                wishlist.modifyWishlistAlarm();
            }
            updateProductStateToSale(product);
        }

        return wishlistAppliedMembers;
    }

    /**
     * 재고를 업데이트한 상품이 품절상태인지 확인합니다.
     *
     * @param product 업데이트 할 상품
     */
    private boolean isSoldOutState(Product product) {
        return product.getProductSaleStateCode().getCodeCategory()
                .equals(ProductSaleState.SOLD_OUT.getName());
    }

    /**
     * 상품의 상태를 판매중으로 변경합니다.
     *
     * @param product 업데이트 할 상품
     */
    private void updateProductStateToSale(Product product) {
        product.modifySaleStateCode(
                productSaleStateCodeRepository
                        .findByCodeCategory(ProductSaleState.SALE.getName())
                        .orElseThrow(NotFoundStateCodeException::new));
    }
}
