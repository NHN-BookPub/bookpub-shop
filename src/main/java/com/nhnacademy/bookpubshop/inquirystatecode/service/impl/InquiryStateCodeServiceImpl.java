package com.nhnacademy.bookpubshop.inquirystatecode.service.impl;

import com.nhnacademy.bookpubshop.inquirystatecode.dto.response.GetInquiryStateCodeResponseDto;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
import com.nhnacademy.bookpubshop.inquirystatecode.service.InquiryStateCodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryStateCodeServiceImpl implements InquiryStateCodeService {
    private final InquiryStateCodeRepository inquiryStateCodeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetInquiryStateCodeResponseDto> getUsedCodeForMember() {
        return inquiryStateCodeRepository.findUsedCodeForMember();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GetInquiryStateCodeResponseDto> getUsedCodeForAdmin() {
        return inquiryStateCodeRepository.findUsedCodeForAdmin();
    }
}
