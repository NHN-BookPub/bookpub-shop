package com.nhnacademy.bookpubshop.product.service.impl;

import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductAuthorRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상품 서비스의 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPolicyRepository productPolicyRepository;
    private final ProductSaleStateCodeRepository saleStateCodeRepository;
    private final ProductTypeStateCodeRepository typeStateCodeRepository;
    private final ProductAuthorRepository productAuthorRepository;
    private final AuthorRepository authorRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GetProductDetailResponseDto getProductDetailById(Long id) {
        return productRepository.getProductDetailById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductDetailResponseDto createProduct(CreateProductRequestDto request) {
        ProductPolicy productPolicy = productPolicyRepository
                .findById(request.getProductPolicyNo())
                .orElseThrow(NotFoundProductPolicyException::new);

        ProductTypeStateCode typeStateCode = typeStateCodeRepository
                .findById(request.getTypeCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        ProductSaleStateCode saleStateCode = saleStateCodeRepository
                .findById(request.getSaleCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        List<Product> relationProducts = new ArrayList<>();

        for (Long relationProductNo : request.getRelationProducts()) {
            relationProducts.add(
                    productRepository.findById(relationProductNo)
                            .orElseThrow(ProductNotFoundException::new));
        }

        Product product = productRepository.save(
                new Product(null,
                        productPolicy,
                        typeStateCode,
                        saleStateCode,
                        relationProducts,
                        request.getProductIsbn(),
                        request.getTitle(),
                        request.getProductPublisher(),
                        request.getPageCount(),
                        request.getProductDescription(),
                        request.getThumbnailPath(),
                        request.getEbookPath(),
                        request.getSalePrice(),
                        request.getProductPrice(),
                        getSaleRateWithSalePrice(
                                request.getProductPrice(),
                                request.getSalePrice()),
                        0L,
                        request.getProductPriority(),
                        false,
                        request.getProductStock(),
                        request.getPublishedAt(),
                        request.isSubscribed()));

        if (!request.getAuthorNos().isEmpty()) {
            for (Integer authorNo : request.getAuthorNos()) {
                productAuthorRepository.save(
                        new ProductAuthor(
                                new ProductAuthor.Pk(authorNo,
                                        product.getProductNo()),
                                authorRepository.findById(authorNo)
                                        .orElseThrow(NotFoundAuthorException::new),
                                productRepository.findById(product.getProductNo())
                                        .orElseThrow(ProductNotFoundException::new))
                );
            }
        }

        return new GetProductDetailResponseDto(
                product.getProductNo(),
                product.getProductIsbn(),
                product.getTitle(),
                product.getPageCount(),
                product.getProductDescription(),
                product.getProductThumbnail(),
                product.getSalesPrice(),
                product.getSalesRate(),
                product.getProductPriority(),
                product.getProductStock(),
                product.getPublishDate(),
                product.isProductDeleted(),
                product.isProductSubscribed(),
                product.getProductSaleStateCode(),
                product.getProductTypeStateCode(),
                product.getProductPolicy()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        Page<GetProductListResponseDto> response =
                productRepository.getAllProducts(pageable);

        if (response.getContent().isEmpty() || response.getTotalElements() == 0) {
            throw new ProductNotFoundException();
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductListResponseDto> getProductListLikeTitle(
            String title, Pageable pageable) {
        return productRepository.getProductListLikeTitle(title, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GetProductDetailResponseDto modifyProduct(CreateProductRequestDto request, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        ProductPolicy productPolicy = productPolicyRepository
                .findById(request.getProductPolicyNo())
                .orElseThrow(NotFoundProductPolicyException::new);

        ProductSaleStateCode saleStateCode = saleStateCodeRepository
                .findById(request.getSaleCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        ProductTypeStateCode typeStateCode = typeStateCodeRepository
                .findById(request.getTypeCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        List<Product> relationProducts = new ArrayList<>();

        for (Long relationProductNo : request.getRelationProducts()) {
            relationProducts.add(
                    productRepository.findById(relationProductNo)
                            .orElseThrow(ProductNotFoundException::new));
        }

        Product save = productRepository.save(new Product(
                            product.getProductNo(),
                            productPolicy,
                            typeStateCode,
                            saleStateCode,
                            relationProducts,
                            request.getProductIsbn(),
                            request.getTitle(),
                            request.getProductPublisher(),
                            request.getPageCount(),
                            request.getProductDescription(),
                            request.getThumbnailPath(),
                            request.getEbookPath(),
                            request.getSalePrice(),
                            request.getProductPrice(),
                            getSaleRateWithSalePrice(
                                    request.getProductPrice(),
                                    request.getSalePrice()),
                            product.getViewCount(),
                            request.getProductPriority(),
                            product.isProductDeleted(),
                            request.getProductStock(),
                            request.getPublishedAt(),
                            request.isSubscribed()));

        return new GetProductDetailResponseDto(
                save.getProductNo(),
                save.getProductIsbn(),
                save.getTitle(),
                save.getPageCount(),
                save.getProductDescription(),
                save.getProductThumbnail(),
                save.getSalesPrice(),
                save.getSalesRate(),
                save.getProductPriority(),
                save.getProductStock(),
                save.getPublishDate(),
                save.isProductDeleted(),
                save.isProductSubscribed(),
                saleStateCode,
                typeStateCode,
                save.getProductPolicy()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void setDeleteProduct(Long id, boolean deleted) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        Product modified = new Product(
                product.getProductNo(),
                product.getProductPolicy(),
                product.getProductTypeStateCode(),
                product.getProductSaleStateCode(),
                product.getRelationProduct(),
                product.getProductIsbn(),
                product.getTitle(),
                product.getProductPublisher(),
                product.getPageCount(),
                product.getProductDescription(),
                product.getProductThumbnail(),
                product.getEbookFilePath(),
                product.getSalesPrice(),
                product.getProductPrice(),
                product.getSalesRate(),
                product.getViewCount(),
                product.getProductPriority(),
                deleted,
                product.getProductStock(),
                product.getPublishDate(),
                product.isProductSubscribed());

        productRepository.save(modified);
    }

    /**
     * 할인율을 계산하여 반환합니다.
     *
     * @param originPrice 정가입니다.
     * @param salePrice   할인가입니다.
     * @return 할인율을 반환합니다.
     */
    private Integer getSaleRateWithSalePrice(Long originPrice, Long salePrice) {
        return Math.toIntExact((originPrice - salePrice) / originPrice * 100);
    }
}
