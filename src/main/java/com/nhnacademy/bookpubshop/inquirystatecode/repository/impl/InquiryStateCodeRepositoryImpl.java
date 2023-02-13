package com.nhnacademy.bookpubshop.inquirystatecode.repository.impl;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.QInquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepositoryCustom;
import com.nhnacademy.bookpubshop.state.InquiryState;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
public class InquiryStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements InquiryStateCodeRepositoryCustom {
    QInquiryStateCode inquiryStateCode = QInquiryStateCode.inquiryStateCode;

    public InquiryStateCodeRepositoryImpl() {
        super(InquiryStateCode.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetInquiryStateCodeResponseDto> findUsedCodeForMember() {
        return from(inquiryStateCode)
                .where(inquiryStateCode.inquiryCodeUsed.isTrue()
                        .and(inquiryStateCode.inquiryCodeName.ne(
                                InquiryState.ANSWER.getName()
                        )))
                .select(Projections.constructor(
                        GetInquiryStateCodeResponseDto.class,
                        inquiryStateCode.inquiryCodeNo,
                        inquiryStateCode.inquiryCodeName
                ))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetInquiryStateCodeResponseDto> findUsedCodeForAdmin() {
        return from(inquiryStateCode)
                .where(inquiryStateCode.inquiryCodeUsed.isTrue()
                        .and(inquiryStateCode.inquiryCodeName.eq(
                                InquiryState.ANSWER.getName()
                        )))
                .select(Projections.constructor(
                        GetInquiryStateCodeResponseDto.class,
                        inquiryStateCode.inquiryCodeNo,
                        inquiryStateCode.inquiryCodeName
                ))
                .fetch();
    }
}
