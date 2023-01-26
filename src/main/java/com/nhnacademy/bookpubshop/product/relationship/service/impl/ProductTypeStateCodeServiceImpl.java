package com.nhnacademy.bookpubshop.product.relationship.service.impl;

import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodesException;
import com.nhnacademy.bookpubshop.product.relationship.dto.GetProductTypeStateCodeResponseDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.service.ProductTypeStateCodeService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품유형상태코드 서비스의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class ProductTypeStateCodeServiceImpl implements ProductTypeStateCodeService {
    private final ProductTypeStateCodeRepository productTypeStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GetProductTypeStateCodeResponseDto getTypeStateCodeById(Integer codeNo) {
        ProductTypeStateCode stateCode =
                productTypeStateCodeRepository
                        .findById(codeNo)
                        .orElseThrow(NotFoundStateCodeException::new);

        return new GetProductTypeStateCodeResponseDto(
                stateCode.getCodeNo(),
                stateCode.getCodeName(),
                stateCode.isCodeUsed(),
                stateCode.getCodeInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetProductTypeStateCodeResponseDto> getAllTypeStateCodes() {
        List<ProductTypeStateCode> codes = productTypeStateCodeRepository.findAll();

        if (codes.isEmpty()) {
            throw new NotFoundStateCodesException();
        }

        List<GetProductTypeStateCodeResponseDto> response = new ArrayList<>();

        for (ProductTypeStateCode code : codes) {
            response.add(
                    new GetProductTypeStateCodeResponseDto(
                            code.getCodeNo(),
                            code.getCodeName(),
                            code.isCodeUsed(),
                            code.getCodeInfo()));
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductTypeStateCodeResponseDto setUsedTypeCodeById(Integer id, boolean used) {
        ProductTypeStateCode stateCode =
                productTypeStateCodeRepository
                        .findById(id)
                        .orElseThrow(NotFoundStateCodeException::new);

        ProductTypeStateCode resultCode =
                productTypeStateCodeRepository.save(
                        new ProductTypeStateCode(
                                id,
                                stateCode.getCodeName(),
                                used,
                                stateCode.getCodeInfo()));

        return new GetProductTypeStateCodeResponseDto(
                resultCode.getCodeNo(),
                resultCode.getCodeName(),
                resultCode.isCodeUsed(),
                resultCode.getCodeInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetProductTypeStateCodeResponseDto> getAllTypeStateCodesUsed() {
        return productTypeStateCodeRepository.findByAllUsed();

    }
}
