package com.nhnacademy.bookpubshop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String productPublisher;
    private Integer pageCount;
    private String productDescription;
    private Long salesPrice;
    private Long productPrice;
    private Integer salesRate;
    private Integer productPriority;
    private Integer productStock;
//    @DateTimeFormat(pattern = "yyyy:MM:dd'T'HH:mm:ss")
    private String publishDate;
    private boolean deleted;
    private boolean productSubscribed;

    private String saleStateCodeCategory;
    private String typeStateCodeName;

    // TODO: 매입이력 추가

    private String policyMethod;
    private boolean policySaved;
    private Integer policySaveRate;

    private List<String> authors = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> tagsColors = new ArrayList<>();

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
        this.categories = product.getProductCategories().stream()
                .map(m -> m.getCategory().getCategoryName())
                .collect(Collectors.toList());
        this.tags = product.getProductTags().stream()
                .map(m -> m.getTag().getTagName())
                .collect(Collectors.toList());
        this.tagsColors = product.getProductTags().stream()
                .map(m -> m.getTag().getColorCode())
                .collect(Collectors.toList());
    }
}
