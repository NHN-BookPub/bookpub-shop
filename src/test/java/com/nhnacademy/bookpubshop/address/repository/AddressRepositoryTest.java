package com.nhnacademy.bookpubshop.address.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
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
 * 주소 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AddressRepository addressRepository;

    BookPubTier tier;
    Address address;
    Member member;

    @BeforeEach
    void setUp() {
        tier = TierDummy.dummy();
        tier = entityManager.persist(tier);

        member = MemberDummy.dummy(tier);
        member = entityManager.persist(member);

        address = AddressDummy.dummy(member);
        address = entityManager.persist(address);
    }

    @Test
    @DisplayName("주소 등록 테스트")
    void AddressSaveTest() {

        Optional<Address> findAddress = addressRepository.findById(address.getAddressNo());

        assertThat(findAddress).isPresent();
        assertThat(findAddress.get().getAddressNo()).isEqualTo(address.getAddressNo());
        assertThat(findAddress.get().getRoadAddress()).contains(address.getRoadAddress());
        assertThat(findAddress.get().getAddressDetail()).isEqualTo(address.getAddressDetail());
    }

    @Test
    @DisplayName("변경할 주소 찾기 테스트")
    void addressFindMemberExchangeAddressTest(){
        Optional<Address> exchangeAddress = addressRepository.findByMemberExchangeAddress(member.getMemberNo(), address.getAddressNo());

        assertThat(exchangeAddress).isPresent();
        assertThat(exchangeAddress.get().getAddressNo()).isEqualTo(address.getAddressNo());
        assertThat(exchangeAddress.get().getAddressDetail()).isEqualTo(address.getAddressDetail());
        assertThat(exchangeAddress.get().getRoadAddress()).isEqualTo(address.getRoadAddress());

    }

    @Test
    @DisplayName("기준 주소지 찾기 테스트")
    void addressMemberBaseAddressTest(){
        Optional<Address> baseAddress = addressRepository.findByMemberBaseAddress(member.getMemberNo());

        assertThat(baseAddress).isPresent();
        assertThat(baseAddress.get().getAddressNo()).isEqualTo(address.getAddressNo());
        assertThat(baseAddress.get().getAddressDetail()).isEqualTo(address.getAddressDetail());
        assertThat(baseAddress.get().getRoadAddress()).isEqualTo(address.getRoadAddress());

    }
}