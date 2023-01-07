package com.nhnacademy.bookpubshop.inquiryanswer.repsitory;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.inquiryanswer.dummy.InquiryAnswerDummy;
import com.nhnacademy.bookpubshop.inquiryanswer.entity.InquiryAnswer;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
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

    InquiryAnswer inquiryAnswer;

    @BeforeEach
    void setUp() {
        inquiryAnswer = InquiryAnswerDummy.dummy(PersonalInquiryDummy.dummy(memberDummy(tierDummy())));
    }

    @DisplayName("1:1문의답변 save 테스트")
    @Test
    void inquiryAnswerSaveTest() {
        entityManager.persist(inquiryAnswer.getPersonalInquiry());
        InquiryAnswer persist = entityManager.persist(inquiryAnswer);
        Optional<InquiryAnswer> result = inquiryAnswerRepository.findById(persist.getAnswerNumber());

        assertThat(result).isPresent();
        assertThat(result.get().getAnswerContent()).isEqualTo(persist.getAnswerContent());
        assertThat(result.get().getAnswerNumber()).isEqualTo(persist.getAnswerNumber());
        assertThat(result.get().getCreatedAt().getYear()).isEqualTo(persist.getCreatedAt().getYear());
        assertThat(result.get().getPersonalInquiry().getPersonalInquiryNo()).
                isEqualTo(persist.getPersonalInquiry().getPersonalInquiryNo());

    }

    private BookPubTier tierDummy() {
        return new BookPubTier(null, "tier");
    }

    private Member memberDummy(BookPubTier bookPubTier) {
        Member testMember = new Member(null, bookPubTier, "test_id", "test_nickname", "test_name", "남", 22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false, null, 0L, false);
        entityManager.persist(testMember.getBookPubTier());
        return entityManager.persist(testMember);
    }
}