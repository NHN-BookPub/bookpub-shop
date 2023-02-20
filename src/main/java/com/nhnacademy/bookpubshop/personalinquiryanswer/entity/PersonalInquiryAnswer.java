package com.nhnacademy.bookpubshop.personalinquiryanswer.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class PersonalInquiryAnswer extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_inquiry_answer_number")
    private Long answerNo;

    @NotNull
    @OneToOne
    @JoinColumn(name = "personal_inquiry_number")
    private PersonalInquiry personalInquiry;

    @NotNull
    @Column(name = "personal_inquiry_content")
    private String answerContent;

    /**
     * Id 값을 제외한 생성자입니다.
     *
     * @param personalInquiry the personal inquiry
     * @param answerContent   the answer content
     */
    @Builder
    public PersonalInquiryAnswer(PersonalInquiry personalInquiry,
                                 String answerContent) {
        this.personalInquiry = personalInquiry;
        this.answerContent = answerContent;
    }
}
