package com.nhnacademy.bookpubshop.inquiryanswer.repsitory;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquiryanswer.dummy.InquiryAnswerDummy;
import com.nhnacademy.bookpubshop.inquiryanswer.entity.InquiryAnswer;
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
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class InquiryAnswerRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    InquiryAnswerRepository inquiryAnswerRepository;

    BookPubTier tier;
    Member member;
    PersonalInquiry personalInquiry;
    InquiryAnswer inquiryAnswer;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        personalInquiry = PersonalInquiryDummy.dummy(member);
        inquiryAnswer = InquiryAnswerDummy.dummy(personalInquiry);

        entityManager.persist(tier);
        entityManager.persist(member);
        entityManager.persist(personalInquiry);
    }

    @DisplayName("1:1문의답변 save 테스트")
    @Test
    void inquiryAnswerSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        InquiryAnswer persist = entityManager.persist(inquiryAnswer);
        Optional<InquiryAnswer> result = inquiryAnswerRepository.findById(persist.getAnswerNumber());

        assertThat(result).isPresent();
        assertThat(result.get().getAnswerContent()).isEqualTo(persist.getAnswerContent());
        assertThat(result.get().getAnswerNumber()).isEqualTo(persist.getAnswerNumber());
        assertThat(result.get().getPersonalInquiry().getPersonalInquiryNo()).
                isEqualTo(persist.getPersonalInquiry().getPersonalInquiryNo());
        assertThat(result.get().getCreatedAt()).isAfter(now);
    }

}