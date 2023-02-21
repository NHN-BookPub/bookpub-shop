package com.nhnacademy.bookpubshop.product.service.impl;

import com.nhnacademy.bookpubshop.author.entity.Author;
import com.nhnacademy.bookpubshop.author.exception.NotFoundAuthorException;
import com.nhnacademy.bookpubshop.author.repository.AuthorRepository;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookpubshop.category.repository.CategoryRepository;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.file.exception.FileNotFoundException;
import com.nhnacademy.bookpubshop.filemanager.FileManagement;
import com.nhnacademy.bookpubshop.filemanager.dto.response.GetDownloadInfo;
import com.nhnacademy.bookpubshop.product.dto.request.CreateProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.CreateRelationProductRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductAuthorRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductCategoryRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductDescriptionRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductInfoRequestDto;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductTagRequestDto;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductByCategoryResponseDto;
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
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.product.service.ProductService;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 상품 서비스 구현체.
 *
 * @author : 여운석, 박경서, 정유진, 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPolicyRepository productPolicyRepository;
    private final ProductSaleStateCodeRepository saleStateCodeRepository;
    private final ProductTypeStateCodeRepository typeStateCodeRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final FileManagement fileManagement;

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
    public void createProduct(CreateProductRequestDto request, Map<String, MultipartFile> fileMap)
            throws IOException {
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
                    new ProductAuthor.Pk(
                            author.getAuthorNo(),
                            product.getProductNo()),
                    author,
                    product));
        }

        List<Integer> categoriesNo = request.getCategoriesNo();
        for (Integer categoryNo : categoriesNo) {
            Category category = categoryRepository.findById(categoryNo)
                    .orElseThrow(CategoryNotFoundException::new);

            product.getProductCategories().add(new ProductCategory(
                    new ProductCategory.Pk(category.getCategoryNo(),
                            product.getProductNo()),
                    category, product));
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

        List<File> images = product.getFiles();

        for (FileCategory category : FileCategory.values()) {
            if (fileMap.get(category.getCategory()) != null) {
                images.add(fileManagement.saveFile(
                        null,
                        null,
                        product,
                        null,
                        null,
                        null,
                        fileMap.get(category.getCategory()),
                        category.getCategory(),
                        category.getPath()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductListResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.getAllProducts(pageable);
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

        productRepository.save(new Product(
                product.getProductNo(),
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
                product.getViewCount(),
                request.getProductPriority(),
                product.isProductDeleted(),
                null,
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


    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetProductDetailResponseDto> getProductsInCart(List<Long> productsNo) {
        return productRepository.getProductsInCart(productsNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductByCategoryResponseDto> getProductsByCategory(
            Integer categoryNo, Pageable pageable) {
        return productRepository.getProductsByCategory(categoryNo, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductByCategoryResponseDto> getEbooks(Pageable pageable) {
        return productRepository.getEbooks(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GetProductByCategoryResponseDto> getEbooksByMember(
            Pageable pageable, Long memberNo) {
        return productRepository.getEbooksByMember(pageable, memberNo);
    }

    @Override
    @Transactional
    public void modifyProductInfo(Long productNo, ModifyProductInfoRequestDto request) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);
        product.modifyProductInfo(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductCategory(Long productNo, ModifyProductCategoryRequestDto request) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        product.initCategory();

        for (Integer categoryNo : request.getCategoriesNo()) {
            Category category = categoryRepository.findById(categoryNo)
                    .orElseThrow(CategoryNotFoundException::new);
            product.getProductCategories()
                    .add(new ProductCategory(
                            new ProductCategory.Pk(categoryNo, productNo),
                            category, product));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductAuthor(Long productNo, ModifyProductAuthorRequestDto request) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        product.initAuthor();

        for (Integer authorNo : request.getAuthors()) {
            Author author = authorRepository.findById(authorNo)
                    .orElseThrow(NotFoundAuthorException::new);

            product.getProductAuthors()
                    .add(new ProductAuthor(
                            new ProductAuthor.Pk(authorNo, productNo),
                            author, product));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductTag(Long productNo, ModifyProductTagRequestDto request) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);
        product.initTag();

        if (Objects.nonNull(request.getTags())) {
            for (Integer tagNo : request.getTags()) {
                Tag tag = tagRepository.findById(tagNo)
                        .orElseThrow(() -> new TagNotFoundException(tagNo));

                product.getProductTags()
                        .add(new ProductTag(new ProductTag.Pk(tagNo, productNo), tag, product));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductType(Long productNo, Integer typeStateNo) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        ProductTypeStateCode typeStateCode = typeStateCodeRepository.findById(typeStateNo)
                .orElseThrow(NotFoundStateCodeException::new);

        product.modifyType(typeStateCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductSale(Long productNo, Integer saleStateNo) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);
        ProductSaleStateCode productSaleStateCode = saleStateCodeRepository.findById(saleStateNo)
                .orElseThrow(NotFoundStateCodeException::new);

        product.modifySaleType(productSaleStateCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductPolicy(Long productNo, Integer policyNo) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);
        ProductPolicy productPolicy = productPolicyRepository.findById(policyNo)
                .orElseThrow(NotFoundProductPolicyException::new);

        product.modifyPolicy(productPolicy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GetDownloadInfo getEbookInfo(Long productNo) {
        String filePath = productRepository.getFilePath(productNo);

        return fileManagement.downloadFileInfo(filePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductDescription(Long productNo,
                                         ModifyProductDescriptionRequestDto request) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        product.modifyDescription(request.getDescription());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductEbook(Long productNo, MultipartFile ebook) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        try {
            Optional<File> ebookFile = product.getFiles().stream()
                    .filter(x -> x.getFileCategory()
                            .equals(FileCategory.PRODUCT_EBOOK.getCategory()))
                    .findFirst();
            if (ebookFile.isEmpty()) {
                return;
            }
            fileManagement.deleteFile(ebookFile.get().getFilePath());
        } catch (IOException e) {
            throw new FileNotFoundException();
        }

        try {
            File file = fileManagement.saveFile(null, null, product,
                    null, null, null,
                    ebook, FileCategory.PRODUCT_EBOOK.getCategory(),
                    FileCategory.PRODUCT_EBOOK.getPath());

            product.modifyEbook(file);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductImage(Long productNo, MultipartFile image) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        try {
            Optional<File> thumbnailImage = product.getFiles().stream()
                    .filter(x -> x.getFileCategory()
                            .equals(FileCategory.PRODUCT_THUMBNAIL.getCategory()))
                    .findFirst();
            if (thumbnailImage.isEmpty()) {
                return;
            }
            fileManagement.deleteFile(thumbnailImage.get().getFilePath());
        } catch (IOException e) {
            throw new FileNotFoundException();
        }

        try {
            File file = fileManagement.saveFile(null, null, product, null, null, null,
                    image, FileCategory.PRODUCT_THUMBNAIL.getCategory(),
                    FileCategory.PRODUCT_THUMBNAIL.getPath());
            product.modifyThumbnail(file);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyProductDetailImage(Long productNo, MultipartFile detail) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        try {
            Optional<File> detailImage = product.getFiles().stream()
                    .filter(x -> x.getFileCategory()
                            .equals(FileCategory.PRODUCT_DETAIL.getCategory()))
                    .findFirst();
            if (detailImage.isEmpty()) {
                return;
            }
            fileManagement.deleteFile(detailImage.get().getFilePath());
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        try {
            File file = fileManagement.saveFile(null, null, product, null, null, null,
                    detail, FileCategory.PRODUCT_DETAIL.getCategory(),
                    FileCategory.PRODUCT_DETAIL.getPath());

            product.modifyDetailImage(file);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addProductImage(Long productNo, MultipartFile image) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        try {
            File file = fileManagement.saveFile(null, null, product, null, null, null,
                    image, FileCategory.PRODUCT_THUMBNAIL.getCategory(),
                    FileCategory.PRODUCT_THUMBNAIL.getPath());

            product.addFile(file);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addProductDetailImage(Long productNo, MultipartFile detailImage) {
        Product product = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        try {
            File file = fileManagement.saveFile(null, null, product, null, null, null,
                    detailImage, FileCategory.PRODUCT_DETAIL.getCategory(),
                    FileCategory.PRODUCT_DETAIL.getPath());

            product.addFile(file);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addRelationProduct(Long productNo, CreateRelationProductRequestDto request) {
        Product parentProduct = productRepository.findById(productNo)
                .orElseThrow(ProductNotFoundException::new);

        for (Long relationProductNo : request.getRelationProducts()) {
            Product product = productRepository.findById(relationProductNo)
                    .orElseThrow(ProductNotFoundException::new);

            product.addParentProduct(parentProduct);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteRelationProduct(Long childNo) {
        Product childProduct = productRepository.findById(childNo)
                .orElseThrow(ProductNotFoundException::new);

        childProduct.deleteParentProduct();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetProductByCategoryResponseDto> getProductsByTypes(Integer typeNo,
                                                                    Pageable pageable) {
        return productRepository.getProductsByTypes(typeNo, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPurchaseUser(String memberNo, String productNo) {
        return productRepository.isPurchaseUser(memberNo, productNo);
    }
}
