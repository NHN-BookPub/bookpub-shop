package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import com.nhnacademy.bookpubshop.product.relationship.dto.CreateProductSaleStateCodeRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductSaleStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductSaleStateCodeService;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품판매유형코드 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class ProductSaleStateCodeServiceImpl implements ProductSaleStateCodeService {
    private final ProductSaleStateCodeRepository productSaleStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductSaleStateCodeResponseDto createSaleCode(
            CreateProductSaleStateCodeRequestDto request) {
        ProductSaleStateCode stateCode =
                productSaleStateCodeRepository.save(
                        new ProductSaleStateCode(
                                null,
                                request.getCodeCategory(),
                                request.isCodeUsed(),
                                request.getCodeInfo()));



        return new GetProductSaleStateCodeResponseDto(
                stateCode.getCodeNumber(),
                stateCode.getCodeCategory(),
                stateCode.isCodeUsed(),
                stateCode.getCodeInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetProductSaleStateCodeResponseDto getSaleCodeById(Integer id) {
        ProductSaleStateCode productSaleStateCode =
                productSaleStateCodeRepository
                        .findById(id)
                        .orElseThrow(NotFoundStateCodeException::new);

        return new GetProductSaleStateCodeResponseDto(
                productSaleStateCode.getCodeNumber(),
                productSaleStateCode.getCodeCategory(),
                productSaleStateCode.isCodeUsed(),
                productSaleStateCode.getCodeInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductSaleStateCodeResponseDto setUsedSaleCodeById(Integer id, boolean used) {
        ProductSaleStateCode stateCode =
                productSaleStateCodeRepository
                        .findById(id)
                        .orElseThrow(NotFoundStateCodeException::new);

        ProductSaleStateCode resultCode = productSaleStateCodeRepository.save(
                new ProductSaleStateCode(id,
                stateCode.getCodeCategory(),
                used,
                stateCode.getCodeInfo()));

        return new GetProductSaleStateCodeResponseDto(
                resultCode.getCodeNumber(),
                resultCode.getCodeCategory(),
                resultCode.isCodeUsed(),
                resultCode.getCodeInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductSaleStateCodeResponseDto> getAllProductSaleStateCode() {
        List<ProductSaleStateCode> codes = productSaleStateCodeRepository.findAll();

        if (codes.isEmpty()) {
            throw new NotFoundStateCodesException();
        }

        List<GetProductSaleStateCodeResponseDto> responseDto = new ArrayList<>();

        for (ProductSaleStateCode code : codes) {
            responseDto.add(new GetProductSaleStateCodeResponseDto(
                    code.getCodeNumber(),
                    code.getCodeCategory(),
                    code.isCodeUsed(),
                    code.getCodeInfo()
            ));
        }
        return responseDto;
    }
}
