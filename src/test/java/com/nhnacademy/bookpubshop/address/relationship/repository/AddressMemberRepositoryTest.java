package com.nhnacademy.bookpubshop.address.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.address.relationship.entity.AddressMember;
import com.nhnacademy.bookpubshop.address.relationship.dummy.AddressMemberDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 주소 멤버 연관관계 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class AddressMemberRepositoryTest {
    @Autowired
    AddressMemberRepository addressMemberRepository;

    @Autowired
    TestEntityManager entityManager;

    BookPubTier bookPubTier;
    Member member;
    Address address;
    AddressMember addressMember;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        address = AddressDummy.dummy();
        addressMember = AddressMemberDummy.dummy(member,address);
    }

    @Test
    @DisplayName("멤버와 주소의 연관관계 테이블 저장")
    void AddressMemberSaveTest() {
        entityManager.persist(addressMember.getMember().getTier());
        entityManager.persist(addressMember.getMember());
        entityManager.persist(addressMember.getAddress());
        entityManager.persist(addressMember);
        entityManager.flush();
        entityManager.clear();

        Optional<AddressMember> findAddressMember = addressMemberRepository.findById(addressMember.getId());

        assertThat(findAddressMember).isPresent();
        assertThat(findAddressMember.get().getAddress().getAddressZipcode()).isEqualTo("61910");
        assertThat(findAddressMember.get().getMember().getMemberNickname()).isEqualTo("nickname");
        assertThat(findAddressMember.get().getId()).isEqualTo(addressMember.getId());

    }
}