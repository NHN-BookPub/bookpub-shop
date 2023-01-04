package com.nhnacademy.bookpubshop.personalinquiry.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
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
public class PersonalInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_inquiry_number", nullable = false, unique = true)
    private Long personalInquiryNo;

    @ManyToOne
    @JoinColumn(name = "member_number", nullable = false)
    private Member member;

    @Column(name = "personal_inquiry_title", nullable = false)
    private String inquiryTitle;

    @Column(name = "personal_inquiry_content", nullable = false)
    private String inquiryContent;

    @Column(name = "personal_inquiry_image_path")
    private String imagePath;

    @Column(name = "personal_inquiry_answered", nullable = false)
    private boolean inquiryAnswered;

    @Column(name = "personal_inquiry_created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "personal_inquiry_deleted", nullable = false)
    private boolean inquiryDeleted;
}
