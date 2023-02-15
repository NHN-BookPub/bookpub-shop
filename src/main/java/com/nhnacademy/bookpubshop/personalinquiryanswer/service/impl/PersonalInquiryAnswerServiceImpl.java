package com.nhnacademy.bookpubshop.personalinquiryanswer.service.impl;

import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiry.exception.PersonalInquiryNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiry.repository.PersonalInquiryRepository;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dto.request.CreatePersonalInquiryAnswerRequestDto;
import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.PersonalInquiryAnswer;
import com.nhnacademy.bookpubshop.personalinquiryanswer.exception.PersonalInquiryAnswerNotFoundException;
import com.nhnacademy.bookpubshop.personalinquiryanswer.repsitory.PersonalInquiryAnswerRepository;
import com.nhnacademy.bookpubshop.personalinquiryanswer.service.PersonalInquiryAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1대1문의답변 서비스 구현체입니다.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalInquiryAnswerServiceImpl implements PersonalInquiryAnswerService {
    private final PersonalInquiryAnswerRepository personalInquiryAnswerRepository;
    private final PersonalInquiryRepository personalInquiryRepository;

    /**
     * {@inheritDoc}
     *
     * @throws PersonalInquiryNotFoundException 1대1문의를 찾을 수 없을때 발생하는 에러
     */
    @Transactional
    @Override
    public void createPersonalInquiryAnswer(CreatePersonalInquiryAnswerRequestDto createDto) {
        PersonalInquiry personalInquiry = personalInquiryRepository
                .findById(createDto.getPersonalInquiryNo())
                .orElseThrow(PersonalInquiryNotFoundException::new);
        personalInquiry.changeInquiryAnswered();

        personalInquiryAnswerRepository.save(PersonalInquiryAnswer.builder()
                .personalInquiry(personalInquiry)
                .answerContent(createDto.getPersonalInquiryAnswerContent())
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @throws PersonalInquiryAnswerNotFoundException 1대1문의답변을 찾을 수 없을때 발생하는 에러
     * @throws PersonalInquiryNotFoundException       1대1문의를 찾을 수 없을때 발생하는 에러
     */
    @Transactional
    @Override
    public void deletePersonalInquiryAnswer(Long answerNo) {
        PersonalInquiryAnswer personalInquiryAnswer =
                personalInquiryAnswerRepository.findById(answerNo)
                        .orElseThrow(PersonalInquiryAnswerNotFoundException::new);

        PersonalInquiry personalInquiry = personalInquiryRepository.findById(
                        personalInquiryAnswer.getPersonalInquiry().getPersonalInquiryNo())
                .orElseThrow(PersonalInquiryNotFoundException::new);
        personalInquiry.changeInquiryAnswered();

        personalInquiryAnswerRepository.delete(personalInquiryAnswer);
    }
}
