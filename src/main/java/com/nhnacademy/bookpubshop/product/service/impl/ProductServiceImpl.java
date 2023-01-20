package com.nhnacademy.bookpubshop.product.service.impl;

import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.product.dto.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByTypeResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductDetailResponseDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListResponseDto;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundProductPolicyException;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductAuthorRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

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
    public void createProduct(CreateProductRequestDto request) {
        ProductPolicy productPolicy = productPolicyRepository
                .findById(request.getProductPolicyNo())
                .orElseThrow(NotFoundProductPolicyException::new);

        ProductTypeStateCode typeStateCode = typeStateCodeRepository
                .findById(request.getTypeCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        ProductSaleStateCode saleStateCode = saleStateCodeRepository
                .findById(request.getSaleCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        Product product = productRepository.save(
                new Product(
                        null,
                        productPolicy,
                        typeStateCode,
                        saleStateCode,
                        null,
                        request.getProductIsbn(),
                        request.getTitle(),
                        request.getProductPublisher(),
                        request.getPageCount(),
                        request.getProductDescription(),
                        request.getSalePrice(),
                        request.getProductPrice(),
                        request.getSalesRate(),
                        0L,
                        request.getProductPriority(),
                        false,
                        request.getProductStock(),
                        request.getPublishedAt(),
                        request.isSubscribed()));

        List<Integer> authorsNo = request.getAuthorsNo();
        for (Integer authorNo : authorsNo) {
            Author author = authorRepository.findById(authorNo)
                    .orElseThrow(NotFoundAuthorException::new);

            product.getProductAuthors().add(new ProductAuthor(
                    new ProductAuthor.Pk(author.getAuthorNo(), product.getProductNo()), author, product));
        }

        List<Integer> categoriesNo = request.getCategoriesNo();
        for (Integer categoryNo : categoriesNo) {
            Category category = categoryRepository.findById(categoryNo)
                    .orElseThrow(CategoryNotFoundException::new);

            product.getProductCategories().add(new ProductCategory(
                    new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()), category, product));
        }

        if (Objects.nonNull(request.getTagsNo())) {
            List<Integer> tagsNo = request.getTagsNo();
            for (Integer tagNo : tagsNo) {
                Tag tag = tagRepository.findById(tagNo)
                        .orElseThrow(() -> new TagNotFoundException(tagNo));

                product.getProductTags().add(new ProductTag(
                        new ProductTag.Pk(tag.getTagNo(), product.getProductNo()), tag, product));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        Page<GetProductListResponseDto> response =
                productRepository.getAllProducts(pageable);

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
    public void modifyProduct(CreateProductRequestDto request, Long id) {
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

//        for (Long relationProductNo : request.getRelationProducts()) {
//            relationProducts.add(
//                    productRepository.findById(relationProductNo)
//                            .orElseThrow(ProductNotFoundException::new));
//        }

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
                request.getSalePrice(),
                request.getProductPrice(),
                request.getSalesRate(),
                product.getViewCount(),
                request.getProductPriority(),
                product.isProductDeleted(),
                null,
//                            request.getProductStock(),
                request.getPublishedAt(),
                request.isSubscribed()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void setDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.modifyProductDeleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetProductByTypeResponseDto> getProductsByType(Integer typeNo, Integer limit) {
        return productRepository.findProductListByType(typeNo, limit);
    }

}
