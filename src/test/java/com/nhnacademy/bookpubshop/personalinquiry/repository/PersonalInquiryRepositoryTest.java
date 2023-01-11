package com.nhnacademy.bookpubshop.personalinquiry.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
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

/**
 * 1:1 상담 Repo Test
 *
 * @author : 유호철
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

    @BeforeEach
    void setUp(){
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        personalInquiry = PersonalInquiryDummy.dummy(member);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
    }

    @Test
    @DisplayName(value = "1:1상품문의 save 테스트")
    void personalInquirySaveTest(){
        LocalDateTime now = LocalDateTime.now();

        entityManager.persist(personalInquiry);

        Optional<PersonalInquiry> result = personalInquiryRepository.findById(personalInquiry.getPersonalInquiryNo());

        assertThat(result).isPresent();
        assertThat(result.get().getPersonalInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(result.get().getInquiryTitle()).isEqualTo(personalInquiry.getInquiryTitle());
        assertThat(result.get().getInquiryContent()).isEqualTo(personalInquiry.getInquiryContent());
        assertThat(result.get().getCreatedAt().getYear()).isEqualTo(personalInquiry.getCreatedAt().getYear());
        assertThat(result.get().getImagePath()).isEqualTo(personalInquiry.getImagePath());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(personalInquiry.getMember().getMemberId());
        assertThat(result.get().isInquiryAnswered()).isFalse();
        assertThat(result.get().isInquiryDeleted()).isFalse();
        assertThat(result.get().getCreatedAt()).isAfter(now);
    }

}