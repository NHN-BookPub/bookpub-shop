package com.nhnacademy.bookpubshop.address.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.relationship.entity.AddressMember;
import com.nhnacademy.bookpubshop.address.relationship.dummy.AddressMemberDummy;
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

    AddressMember addressMember;

    @BeforeEach
    void setUp() {
        addressMember = AddressMemberDummy.dummy();
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
        assertThat(findAddressMember.get().getAddressMemberDetail()).isEqualTo("109동 102호");

    }
}