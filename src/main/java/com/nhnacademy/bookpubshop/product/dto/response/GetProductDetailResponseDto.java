package com.nhnacademy.bookpubshop.product.dto.response;

import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.state.FileCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 상세페이지를 위한 Dto 입니다.
 * ProductPolicy, ProductTypeStateCode, ProductSaleStateCode
 * 위 3가지 클래스를 포함하고 있습니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class GetProductDetailResponseDto {
    private Long productNo;
    private String productIsbn;
    private String title;
    private String thumbnail;
    private String detail;
    private String ebook;
    private String productPublisher;
    private Integer pageCount;
    private String productDescription;
    private Long salesPrice;
    private Long productPrice;
    private Integer salesRate;
    private Integer productPriority;
    private Integer productStock;
    private String publishDate;
    private boolean deleted;
    private boolean productSubscribed;
    private String saleStateCodeCategory;
    private String typeStateCodeName;

    private String policyMethod;
    private boolean policySaved;
    private Integer policySaveRate;

    private List<String> authors = new ArrayList<>();
    private List<Integer> categoriesNo = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> tagsColors = new ArrayList<>();
    private List<GetRelationProductInfoResponseDto> info;

    /**
     * 상품 등록을 위한 DTO 다대다 관계까지 한번에 save.
     *
     * @param product 상품 엔티티
     */
    public GetProductDetailResponseDto(Product product) {
        this.productNo = product.getProductNo();
        this.productIsbn = product.getProductIsbn();
        this.title = product.getTitle();
        this.productPublisher = product.getProductPublisher();
        this.pageCount = product.getPageCount();
        this.productDescription = product.getProductDescription();
        this.salesPrice = product.getSalesPrice();
        this.productPrice = product.getProductPrice();
        this.salesRate = product.getSalesRate();
        this.productPriority = product.getProductPriority();
        this.productStock = product.getProductStock();
        this.publishDate = product.getPublishDate().toString();
        this.deleted = product.isProductDeleted();
        this.productSubscribed = product.isProductSubscribed();
        this.saleStateCodeCategory = product.getProductSaleStateCode().getCodeCategory();
        this.typeStateCodeName = product.getProductTypeStateCode().getCodeName();
        this.policyMethod = product.getProductPolicy().getPolicyMethod();
        this.policySaved = product.getProductPolicy().isPolicySaved();
        this.policySaveRate = product.getProductPolicy().getSaveRate();
        this.authors = product.getProductAuthors().stream()
                .map(m -> m.getAuthor().getAuthorName())
                .collect(Collectors.toList());
        this.categoriesNo = product.getProductCategories().stream()
                .map(m -> m.getCategory().getCategoryNo())
                .collect(Collectors.toList());
        this.categories = product.getProductCategories().stream()
                .map(m -> m.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.tags = product.getProductTags().stream()
                .map(m -> m.getTag().getTagName())
                .collect(Collectors.toList());
        this.tagsColors = product.getProductTags().stream()
                .map(m -> m.getTag().getColorCode())
                .collect(Collectors.toList());

        this.info = new ArrayList<>();

        if (!product.getFiles().isEmpty()) {
            inputFiles(product);
        }
    }

    /**
     * 상품에 연관된 파일들을 dto로 담습니다.(리스트의 크기만큼 반복합니다.)
     *
     * @param product 상품입니다.
     */
    private void inputFiles(Product product) {
        for (int index = 0; index < product.getFiles().size(); index++) {
            inputFilesCheckCategory(product, index);
        }
    }

    /**
     * 상품에 연관된 파일들을 dto로 담습니다.(유형을 확인하여 담아줍니다.)
     *
     * @param product 상품
     * @param index   리스트 인덱스
     */
    private void inputFilesCheckCategory(Product product, int index) {
        if (getFile(product, index).getFileCategory()
                .equals(FileCategory.PRODUCT_THUMBNAIL.getCategory())) {
            this.thumbnail = getFile(product, index).getFilePath();
        } else if (getFile(product, index).getFileCategory()
                .equals(FileCategory.PRODUCT_DETAIL.getCategory())) {
            this.detail = getFile(product, index).getFilePath();
        } else if (getFile(product, index).getFileCategory()
                .equals(FileCategory.PRODUCT_EBOOK.getCategory())) {
            this.ebook = getFile(product, index).getFilePath();
        }
    }

    /**
     * 상품의 파일 리스트에서 원하는 인덱스의 파일을 반환해줍니다.
     *
     * @param product 상품
     * @param index   인덱스
     * @return 파일
     */
    private File getFile(Product product, int index) {
        return product.getFiles().get(index);
    }

    /**
     * 연관관계 상품 정보 저장 메서드.
     *
     * @param info 연관관계 상품 정보
     */
    public void addRelationInfo(List<GetRelationProductInfoResponseDto> info) {
        this.info = info;
    }

    public void addEbookPath(String ebook) {
        this.ebook = ebook;
    }
}
