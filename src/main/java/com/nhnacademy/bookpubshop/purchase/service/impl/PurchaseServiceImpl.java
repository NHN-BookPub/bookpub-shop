package com.nhnacademy.bookpubshop.purchase.service.impl;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.dto.CreatePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseListResponseDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.exception.NotFoundPurchasesException;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import com.nhnacademy.bookpubshop.utils.PageResponse;
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<GetPurchaseListResponseDto> getPurchaseByProductNo(
            Long productNo, Pageable pageable) {
        Page<GetPurchaseListResponseDto> response =
                purchaseRepository.findByProductNumberWithPage(productNo, pageable);

        if (response.getContent().isEmpty()) {
            throw new NotFoundPurchasesException();
        }

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
    public PageResponse<GetPurchaseListResponseDto> getPurchaseListDesc(Pageable pageable) {
        PageResponse<GetPurchaseListResponseDto> result =
                new PageResponse<>(purchaseRepository.getPurchaseListDesc(pageable));

        if (result.getContent().isEmpty()) {
            throw new NotFoundPurchasesException();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void createPurchaseMerged(CreatePurchaseRequestDto request) {
        Product product = productRepository.findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        purchaseRepository.save(request.toEntity(product));

        product.plusStock(request.getPurchaseAmount());

        productRepository.save(product);
    }
}
