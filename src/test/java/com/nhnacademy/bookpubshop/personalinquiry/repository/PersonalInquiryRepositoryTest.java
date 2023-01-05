package com.nhnacademy.bookpubshop.personalinquiry.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.personalinquiry.dummy.PersonalInquiryDummy;
import com.nhnacademy.bookpubshop.personalinquiry.entity.PersonalInquiry;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
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
    Tier tier;
    PersonalInquiry personalInquiry;

    @BeforeEach
    void setUp(){
        tier =  new Tier(null, "test");
        member = memberDummy(tier);
        personalInquiry = PersonalInquiryDummy.dummy(member);
    }
    @Test
    @DisplayName(value = "1:1상품문의 save 테스트")
    void personalInquirySaveTest(){
        entityManager.persist(personalInquiry);
        entityManager.clear();

        Optional<PersonalInquiry> result = personalInquiryRepository.findById(personalInquiry.getPersonalInquiryNo());
        assertThat(result).isPresent();
        assertThat(result.get().getPersonalInquiryNo()).isEqualTo(personalInquiry.getPersonalInquiryNo());
        assertThat(result.get().getInquiryContent()).isEqualTo(personalInquiry.getInquiryContent());
        assertThat(result.get().getCreatedAt().getYear()).isEqualTo(personalInquiry.getCreatedAt().getYear());
        assertThat(result.get().getImagePath()).isEqualTo(personalInquiry.getImagePath());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(personalInquiry.getMember().getMemberId());
    }
    private Member memberDummy(Tier tier){
        Member testMember = new Member(null, tier, "test_id", "test_nickname", "test_name", "남", 22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false, null, 0L, false);
        entityManager.persist(testMember.getTier());
        return entityManager.persist(testMember);
    }

}