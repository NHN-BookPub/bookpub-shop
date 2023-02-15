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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalInquiryAnswerServiceImpl implements PersonalInquiryAnswerService {
    private final PersonalInquiryAnswerRepository personalInquiryAnswerRepository;
    private final PersonalInquiryRepository personalInquiryRepository;

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
