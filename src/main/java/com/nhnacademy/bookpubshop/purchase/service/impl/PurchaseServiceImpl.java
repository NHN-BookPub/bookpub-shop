package com.nhnacademy.bookpubshop.purchase.service.impl;

import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.purchase.dto.GetPurchaseResponseDto;
import com.nhnacademy.bookpubshop.purchase.dto.SavePurchaseRequestDto;
import com.nhnacademy.bookpubshop.purchase.entity.Purchase;
import com.nhnacademy.bookpubshop.purchase.exception.NotFoundPurchasesException;
import com.nhnacademy.bookpubshop.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookpubshop.purchase.service.PurchaseService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 구현 서비스 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetPurchaseResponseDto> getPurchaseByProductNo(Long productNo) {
        List<GetPurchaseResponseDto> response =
                purchaseRepository.findByProductNumber(productNo);

        if (response.isEmpty()) {
            throw new NotFoundPurchasesException();
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetPurchaseResponseDto createPurchase(SavePurchaseRequestDto request) {
        Product product = productRepository
                .findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        Purchase purchase = purchaseRepository.save(
                new Purchase(null,
                        product,
                        request.getPurchasePrice(),
                        request.getPurchaseAmount()));

        return new GetPurchaseResponseDto(
                purchase.getPurchaseNo(),
                purchase.getProduct().getProductNo(),
                purchase.getPurchasePrice(),
                purchase.getCreatedAt(),
                purchase.getPurchaseAmount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetPurchaseResponseDto modifyPurchase(Long purchaseId, SavePurchaseRequestDto request) {
        Purchase purchase = purchaseRepository
                .findById(purchaseId)
                .orElseThrow(NotFoundPurchasesException::new);

        Product product = productRepository
                .findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        Purchase saved = purchaseRepository.save(
                new Purchase(purchase.getPurchaseNo(),
                        product,
                        request.getPurchasePrice(),
                        request.getPurchaseAmount()));

        return new GetPurchaseResponseDto(
                saved.getPurchaseNo(),
                saved.getProduct().getProductNo(),
                saved.getPurchasePrice(),
                saved.getCreatedAt(),
                saved.getPurchaseAmount());
    }
}
