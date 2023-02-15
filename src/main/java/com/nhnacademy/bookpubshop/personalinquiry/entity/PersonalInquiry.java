package com.nhnacademy.bookpubshop.personalinquiry.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 1:1문의 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "personal_inquiry")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PersonalInquiry extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_inquiry_number")
    private Long personalInquiryNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false)
    private Member member;

    @NotNull
    @Column(name = "personal_inquiry_title")
    private String inquiryTitle;

    @NotNull
    @Column(name = "personal_inquiry_content")
    private String inquiryContent;

    @Column(name = "personal_inquiry_answered")
    private boolean inquiryAnswered;

    @Column(name = "personal_inquiry_deleted")
    private boolean inquiryDeleted;

    @Builder
    public PersonalInquiry(Member member, String inquiryTitle, String inquiryContent) {
        this.member = member;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
    }

    public void changeInquiryDeleted() {
        this.inquiryDeleted = !this.inquiryDeleted;
    }

    public void changeInquiryAnswered() {
        this.inquiryAnswered = !this.inquiryAnswered;
    }
}
