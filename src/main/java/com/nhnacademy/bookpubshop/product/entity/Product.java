package com.nhnacademy.bookpubshop.product.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Product 엔티티 맵핑
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
    @Column(name = "product_number")
    private Long productNo;

    @Column(name = "product_isbn")
    private String productIsbn;

    @Column(name = "product_title")
    private String title;

    @Column(name = "product_page_count")
    private Integer pageCount;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "prodcut_thumbnail")
    private String productThumbnail;

    @Column(name = "product_ebook_file_path")
    private String ebookFilePath;

    @Column(name = "product_sales_price")
    private Long salesPrice;

    @Column(name = "product_sales_rate")
    private Integer salesRate;

    @Column(name = "product_view_count")
    private Long viewCount;

    @Column(name = "product_priority")
    private Integer productPriority;

    @Column(name = "product_deleted")
    private boolean productDeleted;

    @Column(name = "product_stock")
    private Integer productStock;

    @Column(name = "product_publish_date")
    private LocalDateTime publishDate;

    @Column(name = "product_created_at")
    private boolean createdAt;

    @Column(name = "product_subscribed")
    private boolean productSubscribed;
}
