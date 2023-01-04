package com.nhnacademy.bookpubshop.product.entity;

import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 상품(product) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_number", nullable = false, unique = true)
    private Long productNo;

    @ManyToOne
    @JoinColumn(name = "product_policy_number", nullable = false)
    private ProductPolicy productPolicy;

    @ManyToOne
    @JoinColumn(name = "product_type_code_number", nullable = false)
    private ProductTypeStateCode productTypeStateCode;

    @ManyToOne
    @JoinColumn(name = "product_sale_code_number", nullable = false)
    private ProductSaleStateCode productSaleStateCode;

    @Column(name = "product_isbn", nullable = false, unique = true)
    private String productIsbn;

    @Column(name = "product_title", nullable = false)
    private String title;

    @Column(name = "product_page_count")
    private Integer pageCount;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_thumbnail")
    private String productThumbnail;

    @Column(name = "product_ebook_file_path")
    private String ebookFilePath;

    @Column(name = "product_sales_price", nullable = false)
    private Long salesPrice;

    @Column(name = "product_sales_rate", nullable = false)
    private Integer salesRate;

    @Column(name = "product_view_count", nullable = false)
    private Long viewCount;

    @Column(name = "product_priority", nullable = false)
    private Integer productPriority;

    @Column(name = "product_deleted", nullable = false)
    private boolean productDeleted;

    @Column(name = "product_stock", nullable = false)
    private Integer productStock;

    @Column(name = "product_published_at", nullable = false)
    private LocalDateTime publishDate;

    @Column(name = "product_created_at", nullable = false)
    private boolean createdAt;

    @Column(name = "product_subscribed", nullable = false)
    private boolean productSubscribed;
}
