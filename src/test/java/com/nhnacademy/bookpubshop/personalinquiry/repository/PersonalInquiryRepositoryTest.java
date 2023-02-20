package com.nhnacademy.bookpubshop.personalinquiry.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetPersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dto.response.GetSimplePersonalInquiryResponseDto;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.personalinquiryanswer.dummy.PersonalInquiryAnswerDummy;
import com.nhnacademy.bookpubshop.personalinquiryanswer.entity.PersonalInquiryAnswer;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 1:1 상담 Repo Test
 *
 * @author : 유호철, 임태원
 * @since : 1.0
 **/
@DataJpaTest
class PersonalInquiryRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PersonalInquiryRepository personalInquiryRepository;

    Member member;
    BookPubTier bookPubTier;
    PersonalInquiry personalInquiry;
    PersonalInquiryAnswer personalInquiryAnswer;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        personalInquiry = PersonalInquiryDummy.dummy(member);
        personalInquiryAnswer = PersonalInquiryAnswerDummy.dummy(personalInquiry);

        entityManager.persist(bookPubTier);
        member = entityManager.persist(member);
        personalInquiry = entityManager.persist(personalInquiry);
        personalInquiryAnswer = entityManager.persist(personalInquiryAnswer);

    }

    @Test
    @DisplayName(value = "1:1상품문의 save 테스트")
    void personalInquirySaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Optional<PersonalInquiry> result = personalInquiryRepository.findById(personalInquiry.getPersonalInquiryNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPersonalInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(result.get().getInquiryTitle()).isEqualTo(personalInquiry.getInquiryTitle());
        assertThat(result.get().getInquiryContent()).isEqualTo(personalInquiry.getInquiryContent());
        assertThat(result.get().getCreatedAt().getYear()).isEqualTo(personalInquiry.getCreatedAt().getYear());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(personalInquiry.getMember().getMemberId());
        assertThat(result.get().isInquiryAnswered()).isFalse();
        assertThat(result.get().isInquiryDeleted()).isFalse();
        assertThat(result.get().getCreatedAt()).isEqualTo(personalInquiry.getCreatedAt());
    }

    @Test
    @DisplayName("회원의 1:1 문의 리스트")
    void findMemberPersonalInquiries() {
        Pageable pageable = Pageable.ofSize(10);
        Page<GetSimplePersonalInquiryResponseDto> response
                = personalInquiryRepository.findMemberPersonalInquiries(pageable, member.getMemberNo());

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(response.getContent().get(0).getMemberNickname()).isEqualTo(member.getMemberNickname());
        assertThat(response.getContent().get(0).getInquiryTitle()).isEqualTo(personalInquiry.getInquiryTitle());
        assertThat(response.getContent().get(0).getCreatedAt()).isEqualTo(personalInquiry.getCreatedAt());
        assertThat(response.getContent().get(0).isInquiryAnswered()).isEqualTo(personalInquiry.isInquiryAnswered());
    }

    @Test
    @DisplayName("모든 회원의 1:1문의 리스트")
    void findPersonalInquiries() {
        Pageable pageable = Pageable.ofSize(10);
        Page<GetSimplePersonalInquiryResponseDto> response
                = personalInquiryRepository.findPersonalInquiries(pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(response.getContent().get(0).getMemberNickname()).isEqualTo(member.getMemberNickname());
        assertThat(response.getContent().get(0).getInquiryTitle()).isEqualTo(personalInquiry.getInquiryTitle());
        assertThat(response.getContent().get(0).getCreatedAt()).isEqualTo(personalInquiry.getCreatedAt());
        assertThat(response.getContent().get(0).isInquiryAnswered()).isEqualTo(personalInquiry.isInquiryAnswered());
    }

    @Test
    @DisplayName("1:1 문의 상세내용")
    void findPersonalInquiry() {
        Optional<GetPersonalInquiryResponseDto> response
                = personalInquiryRepository.findPersonalInquiry(personalInquiry.getPersonalInquiryNo());

        assertThat(response).isPresent();
        assertThat(response.get().getInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(response.get().getMemberNickname()).isEqualTo(member.getMemberNickname());
        assertThat(response.get().getInquiryTitle()).isEqualTo(personalInquiry.getInquiryTitle());
        assertThat(response.get().getCreatedAt()).isEqualTo(personalInquiry.getCreatedAt());
        assertThat(response.get().isInquiryAnswered()).isEqualTo(personalInquiry.isInquiryAnswered());
        assertThat(response.get().getInquiryContent()).isEqualTo(personalInquiry.getInquiryContent());
        assertThat(response.get().getInquiryAnswerContent()).isEqualTo(personalInquiryAnswer.getAnswerContent());
        assertThat(response.get().getInquiryAnswerNo()).isEqualTo(personalInquiryAnswer.getAnswerNo());
        assertThat(response.get().getAnswerCreatedAt()).isEqualTo(personalInquiryAnswer.getCreatedAt());
    }


}