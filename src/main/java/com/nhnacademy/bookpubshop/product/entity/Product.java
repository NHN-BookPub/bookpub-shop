package com.nhnacademy.bookpubshop.product.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_number")
    private Long productNo;

    @ManyToOne
    @JoinColumn(name = "product_policy_number")
    private ProductPolicy productPolicy;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_type_code_number")
    private ProductTypeStateCode productTypeStateCode;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_sale_code_number")
    private ProductSaleStateCode productSaleStateCode;

    @OneToMany
    @JoinColumn(name = "product_relation_number")
    private List<Product> relationProduct = new ArrayList<>();

    @NotNull
    @Column(name = "product_isbn", unique = true)
    private String productIsbn;

    @NotNull
    @Column(name = "product_title")
    private String title;

    @Column(name = "product_page_count")
    private Integer pageCount;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_thumbnail")
    private String productThumbnail;

    @Column(name = "product_ebook_file_path")
    private String ebookFilePath;

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
}
