package com.nhnacademy.bookpubshop.subscribe.entity;

import com.nhnacademy.bookpubshop.file.entity.File;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독(subscribe) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_number")
    private Long subscribeNo;

    @OneToOne(mappedBy = "subscribe", cascade =
            {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private File file;

    @NotNull
    @Column(name = "subscribe_name")
    private String subscribeName;

    @NotNull
    @Column(name = "subscribe_sales_price")
    private Long salesPrice;

    @NotNull
    @Column(name = "subscribe_price")
    private Long subscribePrice;

    @Column(name = "subscribe_sales_rate")
    private Integer salesRate;

    @Column(name = "subscribe_view_count")
    private Long viewCount;

    @Column(name = "subscribe_deleted")
    private boolean subscribeDeleted;

    @Column(name = "subscribe_renewed")
    private boolean subscribeRenewed;

    /**
     * 구독생성자.
     *
     * @param subscribeName  구독이름
     * @param salesPrice     세일가격
     * @param subscribePrice 판매가격
     * @param salesRate      할인퍼센트
     * @param viewCount      뷰 카운트
     * @param renewed        갱신여부
     */
    @Builder
    public Subscribe(String subscribeName,
                     Long salesPrice, Long subscribePrice,
                     Integer salesRate, Long viewCount, boolean renewed) {
        this.subscribeName = subscribeName;
        this.salesPrice = salesPrice;
        this.subscribePrice = subscribePrice;
        this.salesRate = salesRate;
        this.viewCount = 0L;
        this.subscribeRenewed = renewed;
    }

    /**
     * 삭제여부 수정.
     *
     * @param subscribeDeleted 구독상품 삭제여부 설정
     */
    public void changeIsDeleted(boolean subscribeDeleted) {
        this.subscribeDeleted = subscribeDeleted;
    }

    /**
     * 파일을 등록하기위한 메서드입니다.
     *
     * @param file 파일 정보가 기입됩니다.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 구독정보를 수정하기위한 메서드입니다.
     *
     * @param name           구독이름.
     * @param salesPrice     할인 판매가.
     * @param subscribePrice 원가.
     */
    public void modifySubscribeInfo(String name,
                                    Long salesPrice,
                                    Long subscribePrice,
                                    Integer saleRate,
                                    boolean renewed,
                                    boolean deleted) {
        this.subscribeName = name;
        this.salesPrice = salesPrice;
        this.subscribePrice = subscribePrice;
        this.salesRate = saleRate;
        this.subscribeRenewed = renewed;
        this.subscribeDeleted = deleted;
    }
}
