package com.nhnacademy.bookpubshop.personalinquiry.service.impl;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.personalinquiry.dto.request.CreatePersonalInquiryRequestDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.exception.PersonalInquiryNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiry.repository.PersonalInquiryRepository;
import com.nhnacademy.bookpubshop.personalinquiry.service.PersonalInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1대1문의를 다루기 위한 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalInquiryServiceImpl implements PersonalInquiryService {
    private final PersonalInquiryRepository personalInquiryRepository;
    private final MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버를 찾을 수 없을 때 나는 에러
     */
    @Transactional
    @Override
    public void createPersonalInquiry(CreatePersonalInquiryRequestDto createDto) {
        Member member = memberRepository.findById(createDto.getMemberNo())
                .orElseThrow(MemberNotFoundException::new);

        personalInquiryRepository.save(PersonalInquiry.builder()
                .member(member)
                .inquiryTitle(createDto.getInquiryTitle())
                .inquiryContent(createDto.getInquiryContent())
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @throws PersonalInquiryNotFoundException 1대1문의를 찾을 수 없을 때 나는 에러
     */
    @Transactional
    @Override
    public void deletePersonalInquiry(Long personalInquiryNo) {
        PersonalInquiry personalInquiry = personalInquiryRepository.findById(personalInquiryNo)
                .orElseThrow(PersonalInquiryNotFoundException::new);

        personalInquiry.changeInquiryDeleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSimplePersonalInquiryResponseDto> getMemberPersonalInquiries(
            Pageable pageable, Long memberNo) {
        return personalInquiryRepository.findMemberPersonalInquiries(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetSimplePersonalInquiryResponseDto> getPersonalInquiries(Pageable pageable) {
        return personalInquiryRepository.findPersonalInquiries(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws PersonalInquiryNotFoundException 1대1문의를 찾을 수 없을 때 나는 에러
     */
    @Override
    public GetPersonalInquiryResponseDto getPersonalInquiry(Long inquiryNo) {
        return personalInquiryRepository.findPersonalInquiry(inquiryNo)
                .orElseThrow(PersonalInquiryNotFoundException::new);
    }
}
