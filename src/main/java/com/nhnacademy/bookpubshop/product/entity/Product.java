package com.nhnacademy.bookpubshop.product.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductAuthor;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTag;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_relation_number")
    private List<Product> relationProduct = new ArrayList<>();

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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final Set<ProductCategory> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final Set<ProductAuthor> productAuthors = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final Set<ProductTag> productTags = new HashSet<>();

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
     * @param relationProduct      연관관계 상품
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
                   List<Product> relationProduct,
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
        this.relationProduct = relationProduct;
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
    public void minusStock() {
        this.productStock -= 1;
    }
}
