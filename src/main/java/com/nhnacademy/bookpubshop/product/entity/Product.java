package com.nhnacademy.bookpubshop.product.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.product.dto.request.ModifyProductInfoRequestDto;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.FileCategory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품(product) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_number")
    private Long productNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_policy_number")
    private ProductPolicy productPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "product_type_code_number")
    private ProductTypeStateCode productTypeStateCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "product_sale_code_number")
    private ProductSaleStateCode productSaleStateCode;

    @ManyToOne
    @JoinColumn(name = "product_relation_number")
    private Product parentProduct;

    @NotNull
    @Column(name = "product_isbn")
    private String productIsbn;

    @NotNull
    @Column(name = "product_title")
    private String title;

    @NotNull
    @Column(name = "product_publisher")
    private String productPublisher;

    @Column(name = "product_page_count")
    private Integer pageCount;

    @Column(name = "product_description")
    private String productDescription;

    @NotNull
    @Column(name = "product_sales_price")
    private Long salesPrice;

    @NotNull
    @Column(name = "product_price")
    private Long productPrice;

    @Column(name = "product_sales_rate")
    private Integer salesRate;

    @Column(name = "product_view_count")
    private Long viewCount;

    @Column(name = "product_priority")
    private Integer productPriority;

    @Column(name = "product_deleted")
    private boolean productDeleted;

    @NotNull
    @Column(name = "product_stock")
    private Integer productStock;

    @NotNull
    @Column(name = "product_published_at")
    private LocalDateTime publishDate;

    @Column(name = "product_subscribed")
    private boolean productSubscribed;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private final Set<ProductCategory> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private final Set<ProductAuthor> productAuthors = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private final Set<ProductTag> productTags = new HashSet<>();

    @OneToMany(mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<File> files = new ArrayList<>();

    public void setProductFiles(List<File> files) {
        this.files = files;
    }

    public void modifyProductDeleted() {
        this.productDeleted = !this.productDeleted;
    }

    /**
     * 상품 등록을 위한 생성자.
     *
     * @param productNo            상품 번호
     * @param productPolicy        성품 정책
     * @param productTypeStateCode 상품 종류 유형 코드
     * @param productSaleStateCode 상품 판매 유형 코드
     * @param parentProduct        부모 상품
     * @param productIsbn          ISBN
     * @param title                제목
     * @param productPublisher     출판사
     * @param pageCount            페이지수
     * @param productDescription   설명
     * @param salesPrice           판매가
     * @param productPrice         정가
     * @param salesRate            할인율
     * @param viewCount            조회수
     * @param productPriority      상품 우선순위
     * @param productDeleted       삭제여부
     * @param productStock         재고량
     * @param publishDate          출판일
     * @param productSubscribed    구독가능여
     */
    public Product(Long productNo, ProductPolicy productPolicy,
                   ProductTypeStateCode productTypeStateCode,
                   ProductSaleStateCode productSaleStateCode,
                   Product parentProduct,
                   String productIsbn, String title,
                   String productPublisher, Integer pageCount,
                   String productDescription, Long salesPrice,
                   Long productPrice, Integer salesRate,
                   Long viewCount, Integer productPriority,
                   boolean productDeleted, Integer productStock,
                   LocalDateTime publishDate, boolean productSubscribed) {
        this.productNo = productNo;
        this.productPolicy = productPolicy;
        this.productTypeStateCode = productTypeStateCode;
        this.productSaleStateCode = productSaleStateCode;
        this.parentProduct = parentProduct;
        this.productIsbn = productIsbn;
        this.title = title;
        this.productPublisher = productPublisher;
        this.pageCount = pageCount;
        this.productDescription = productDescription;
        this.salesPrice = salesPrice;
        this.productPrice = productPrice;
        this.salesRate = salesRate;
        this.viewCount = viewCount;
        this.productPriority = productPriority;
        this.productDeleted = productDeleted;
        this.productStock = productStock;
        this.publishDate = publishDate;
        this.productSubscribed = productSubscribed;
    }

    /**
     * 상품 매입 시 상품 수량을 올려주는 메소드.
     *
     * @param amount 매입수량.
     */
    public void plusStock(Integer amount) {
        this.productStock += amount;
    }

    /**
     * 주문 시 상품의 수량을 1개 빼주는 메소드.
     */
    public void minusStock(Integer amount) {
        this.productStock -= amount;
    }

    /**
     * 상품의 판매 유형을 변경하는 메서드. <br/>
     * ex)  <br/>
     * 새로운 매입 관리 : 품절 -> 판매중. <br/>
     * 주문 : 판매중 -> 품절   <br/>
     * 상태코드를 변경하는 메서드.
     *
     * @param saleStateCode 변경할 판매 유형 코드
     */
    public void modifySaleStateCode(ProductSaleStateCode saleStateCode) {
        this.productSaleStateCode = saleStateCode;
    }

    /**
     * 상품 정보를 수정하는 메서드.
     *
     * @param request 상품 수정 정보
     */
    public void modifyProductInfo(ModifyProductInfoRequestDto request) {
        this.productIsbn = request.getProductIsbn();
        this.productPrice = request.getProductPrice();
        this.salesRate = request.getSalesRate();
        this.productPublisher = request.getProductPublisher();
        this.publishDate = request.getPublishedAt();
        this.pageCount = request.getPageCount();
        this.salesPrice = request.getSalesPrice();
        this.productPriority = request.getPriority();
    }

    /**
     * 카테고리들을 전부 지우는 메서드.
     */
    public void initCategory() {
        this.productCategories.clear();
    }

    /**
     * 저자들을 전부 지우는 메서드.
     */
    public void initAuthor() {
        this.productAuthors.clear();
    }

    /**
     * 태그들을 전부 지우는 메서드.
     */
    public void initTag() {
        this.productTags.clear();
    }

    /**
     * 상품 유형을 변경하는 메서드.
     *
     * @param productTypeStateCode 변경할 상품 유형
     */
    public void modifyType(ProductTypeStateCode productTypeStateCode) {
        this.productTypeStateCode = productTypeStateCode;
    }

    /**
     * 상품 판매 유형을 변경하는 메서드.
     *
     * @param productSaleStateCode 변경할 판매 유형
     */
    public void modifySaleType(ProductSaleStateCode productSaleStateCode) {
        this.productSaleStateCode = productSaleStateCode;
    }

    /**
     * 상품 포인트 정책을 변경하는 메서드.
     *
     * @param productPolicy 변경할 포인트 정책
     */
    public void modifyPolicy(ProductPolicy productPolicy) {
        this.productPolicy = productPolicy;
    }

    /**
     * 상품 설명을 변경하는 메서드.
     *
     * @param changeDescription 변경할 상품 설명
     */
    public void modifyDescription(String changeDescription) {
        this.productDescription = changeDescription;
    }

    /**
     * E-Book 파일을 변경하는 메서드.
     *
     * @param file 변경할 파일
     */
    public void modifyEbook(File file) {
        this.getFiles().removeIf(tmp -> tmp.getFileCategory()
                .equals(FileCategory.PRODUCT_EBOOK.getCategory()));
        this.getFiles().add(file);
    }

    /**
     * 썸네일 이미지를 변경하는 메서드.
     *
     * @param file 변경할 파일
     */
    public void modifyThumbnail(File file) {
        this.getFiles().removeIf(x -> x.getFileCategory()
                .equals(FileCategory.PRODUCT_THUMBNAIL.getCategory()));
        this.getFiles().add(file);
    }

    /**
     * 상품 상세 이미지를 변경하는 메서드.
     *
     * @param file 변경할 파일
     */
    public void modifyDetailImage(File file) {
        this.getFiles().removeIf(x -> x.getFileCategory()
                .equals(FileCategory.PRODUCT_DETAIL.getCategory()));
        this.getFiles().add(file);
    }

    /**
     * 상품에 파일을 추가하는 메서드.
     * (썸네일, 상세이미지, E-Book).
     *
     * @param file 추가할 파일
     */
    public void addFile(File file) {
        this.getFiles().add(file);
    }

    /**
     * 자식 상품에서 부모 상품을 연관관계로 추가하는 메서드.
     *
     * @param parentProduct 부모 상품
     */
    public void addParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
    }

    /**
     * 자식 상품에서 부모 상품을 연관관계를 삭제하는 메서드.
     */
    public void deleteParentProduct() {
        this.parentProduct = null;
    }

    /**
     * 상품 타입 유형을 변경하는 메서드.
     *
     * @param productTypeStateCode 변경할 상품 타입
     */
    public void modifyProductType(ProductTypeStateCode productTypeStateCode) {
        this.productTypeStateCode = productTypeStateCode;
    }

}
