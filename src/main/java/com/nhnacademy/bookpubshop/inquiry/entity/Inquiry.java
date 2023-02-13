package com.nhnacademy.bookpubshop.inquiry.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품문의(inquiry) 테이블.
 *
 * @author : 박경서
 * @since : 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "inquiry")
public class Inquiry extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_number")
    private Long inquiryNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_parent_number")
    private Inquiry parentInquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "product_number")
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_state_code_number")
    private InquiryStateCode inquiryStateCode;

    @NotNull
    @Column(name = "inquiry_title")
    private String inquiryTitle;

    @NotNull
    @Column(name = "inquiry_content")
    private String inquiryContent;

    @Column(name = "inquiry_displayed")
    private boolean inquiryDisplayed;

    @Column(name = "inquiry_answered")
    private boolean inquiryAnswered;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY)
    private List<File> inquiryImages = new ArrayList<>();

    /**
     * 상품문의 생성을 위한 빌더
     *
     * @param parentInquiry    부모 문의(문의 질문)
     * @param member           회원
     * @param product          상품
     * @param inquiryStateCode 상품문의상태코드
     * @param inquiryTitle     상품문의 제목
     * @param inquiryContent   상품문의 내용
     * @param inquiryDisplayed 상품문의 공개 여부
     */
    @Builder
    public Inquiry(Inquiry parentInquiry, Member member, Product product,
                   InquiryStateCode inquiryStateCode, String inquiryTitle,
                   String inquiryContent, boolean inquiryDisplayed) {
        this.parentInquiry = parentInquiry;
        this.member = member;
        this.product = product;
        this.inquiryStateCode = inquiryStateCode;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryDisplayed = inquiryDisplayed;
    }

    /**
     * 상품문의의 답변 완료 여부를 수정하기 위한 메서드입니다.
     */
    public void modifyAnswered() {
        this.inquiryAnswered = !this.isInquiryAnswered();
    }
}