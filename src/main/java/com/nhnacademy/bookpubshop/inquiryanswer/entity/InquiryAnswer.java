package com.nhnacademy.bookpubshop.inquiryanswer.entity;

import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의답변 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "personal_inquiry_answer")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquiryAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_inquiry_answer_number", nullable = false, unique = true)
    private Long answerNumber;

    @OneToOne
    @JoinColumn(name = "personal_inquiry_number", nullable = false)
    private PersonalInquiry personalInquiry;

    @Column(name = "personal_inquiry_content", nullable = false)
    private String answerContent;

    @Column(name = "personal_inquiry_created_at", nullable = false)
    private LocalDateTime createdAt;
}
