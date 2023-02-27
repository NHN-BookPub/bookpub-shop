package com.nhnacademy.bookpubshop.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dummy.MemberAuthorityDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 멤버 레포지토리 테스트
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    BookPubTier bookPubTier;
    Member member;

    Authority authority;

    MemberAuthority memberAuthority;

    Address address;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        authority = AuthorityDummy.dummy();
        entityManager.persist(bookPubTier);
        memberAuthority = MemberAuthorityDummy.dummy(member, authority);
        address = AddressDummy.dummy(member);
    }

    @Test
    @DisplayName("멤버 save 테스트")
    void memberSaveTest() {
        LocalDateTime now = LocalDateTime.now();

        Member persist = entityManager.persist(member);

        Optional<Member> member = memberRepository.findById(persist.getMemberNo());

        assertThat(member).isPresent();
        assertThat(member.get().getMemberId()).isEqualTo(persist.getMemberId());
        assertThat(member.get().getMemberNickname()).isEqualTo(persist.getMemberNickname());
        assertThat(member.get().getMemberGender()).isEqualTo(persist.getMemberGender());
        assertThat(member.get().getMemberNo()).isEqualTo(persist.getMemberNo());
        assertThat(member.get().getMemberEmail()).isEqualTo(persist.getMemberEmail());
        assertThat(member.get().getMemberPhone()).isEqualTo(persist.getMemberPhone());
        assertThat(member.get().getTier().getTierNo()).isEqualTo(persist.getTier().getTierNo());
        assertThat(member.get().getBlockedAt()).isEqualTo(persist.getBlockedAt());
        assertThat(member.get().getCreatedAt()).isEqualTo(persist.getCreatedAt());
        assertThat(member.get().getMemberBirthYear()).isEqualTo(persist.getMemberBirthYear());
        assertThat(member.get().getMemberBirthMonth()).isEqualTo(persist.getMemberBirthMonth());
        assertThat(member.get().isSocialJoined()).isEqualTo(persist.isSocialJoined());
        assertThat(member.get().isMemberDeleted()).isFalse();
        assertThat(member.get().isMemberBlocked()).isFalse();
        assertThat(member.get().getMemberPoint()).isEqualTo(persist.getMemberPoint());
        assertThat(member.get().getCreatedAt()).isAfter(now);
    }

    @DisplayName("멤버 상세조회 테스트")
    @Test
    void findByMemberDetailsTest() {
        Member persist = entityManager.persist(member);
        entityManager.persist(authority);
        entityManager.persist(memberAuthority);
        entityManager.persist(address);
        entityManager.flush();
        entityManager.clear();

        Optional<MemberDetailResponseDto> result = memberRepository.findByMemberDetails(persist.getMemberNo());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberNo()).isEqualTo(persist.getMemberNo());
        assertThat(result.get().getEmail()).isEqualTo(persist.getMemberEmail());
        assertThat(result.get().getNickname()).isEqualTo(persist.getMemberNickname());
        assertThat(result.get().getTierName()).isEqualTo(bookPubTier.getTierName());
        assertThat(result.get().getAuthorities().get(0)).isEqualTo(authority.getAuthorityName());
        assertThat(result.get().getGender()).isEqualTo(persist.getMemberGender());
        assertThat(result.get().getBirthMonth()).isEqualTo(persist.getMemberBirthMonth());
        assertThat(result.get().getBirthYear()).isEqualTo(persist.getMemberBirthYear());
        assertThat(result.get().getPhone()).isEqualTo(persist.getMemberPhone());
        assertThat(result.get().getPoint()).isEqualTo(persist.getMemberPoint());
        assertThat(result.get().getAddresses().get(0).getAddressDetail()).isEqualTo(address.getAddressDetail());
        assertThat(result.get().getAddresses().get(0).getRoadAddress()).isEqualTo(address.getRoadAddress());
        assertThat(result.get().getAddresses().get(0).isAddressBased()).isEqualTo(address.isAddressMemberBased());
        assertThat(result.get().getAddresses().get(0).getAddressNo()).isEqualTo(address.getAddressNo());
    }

    @DisplayName("멤버 전체조회 테스트")
    @Test
    void findMembersTest() {
        Member persist = entityManager.persist(member);
        Pageable pageable = PageRequest.of(0, 10);

        Page<MemberResponseDto> result = memberRepository.findMembers(pageable);

        List<MemberResponseDto> content = result.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content.get(0).getMemberNo()).isEqualTo(persist.getMemberNo());
        assertThat(content.get(0).getTier()).isEqualTo(persist.getTier().getTierName());
        assertThat(content.get(0).getNickname()).isEqualTo(persist.getMemberNickname());
        assertThat(content.get(0).getName()).isEqualTo(persist.getMemberName());
        assertThat(content.get(0).getGender()).isEqualTo(persist.getMemberGender());
        assertThat(content.get(0).getBirthYear()).isEqualTo(persist.getMemberBirthYear());
        assertThat(content.get(0).getBirthMonth()).isEqualTo(persist.getMemberBirthMonth());
        assertThat(content.get(0).getEmail()).isEqualTo(persist.getMemberEmail());
        assertThat(content.get(0).getPoint()).isEqualTo(persist.getMemberPoint());
        assertThat(content.get(0).isSocial()).isEqualTo(persist.isSocialJoined());
    }

    @DisplayName("멤버 통계")
    @Test
    void memberStatisticsTest() {
        entityManager.persist(member);

        MemberStatisticsResponseDto result = memberRepository.memberStatistics();

        assertThat(result).isNotNull();
        assertThat(result.getMemberCnt()).isEqualTo(1);
        assertThat(result.getBlockMemberCnt()).isZero();
        assertThat(result.getDeleteMemberCnt()).isZero();
        assertThat(result.getCurrentMemberCnt()).isEqualTo(1);
    }

    @DisplayName("멤버 등급별 통계")
    @Test
    void memberTierStatisticsTest() {
        Member persist = entityManager.persist(member);
        Member dummy = MemberDummy.dummy2(bookPubTier);
        entityManager.persist(dummy);

        List<MemberTierStatisticsResponseDto> memberTierStatistics = memberRepository.memberTierStatistics();

        assertThat(memberTierStatistics).isNotEmpty();
        assertThat(memberTierStatistics.get(0).getTierValue().intValue()).isEqualTo(persist.getTier().getTierValue());
        assertThat(memberTierStatistics.get(0).getTierName()).isEqualTo(persist.getTier().getTierName());
        assertThat(memberTierStatistics.get(0).getTierCnt()).isEqualTo(2);
    }

    @DisplayName("로그인 멤버 조회 테스트")
    @Test
    void findLoginMemberSuccessTest() {
        Member memberPersist = entityManager.persist(member);
        entityManager.persist(authority);
        entityManager.persist(memberAuthority);
        entityManager.flush();
        entityManager.clear();

        LoginMemberResponseDto result
                = memberRepository.findByMemberLoginInfo(memberPersist.getMemberId());

        assertThat(result.getMemberNo()).isEqualTo(memberPersist.getMemberNo());
        assertThat(result.getMemberPwd()).isEqualTo(memberPersist.getMemberPwd());
        assertThat(result.getMemberId()).isEqualTo(memberPersist.getMemberId());
    }

    @DisplayName("로그인 멤버 조회 실패 테스트")
    @Test
    void findLoginMemberFailedTest() {
        entityManager.persist(member);
        entityManager.persist(authority);
        entityManager.persist(new MemberAuthority(
                new MemberAuthority.Pk(1L, 1), member, authority
        ));
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> memberRepository.findByMemberLoginInfo("failId"))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @DisplayName("인증받은 회원정보 불러오기 성공")
    @Test
    void findAuthMember() {
        entityManager.persist(member);
        entityManager.persist(authority);
        entityManager.persist(memberAuthority);

        MemberAuthResponseDto result
                = memberRepository.findByAuthMemberInfo(member.getMemberNo());

        assertThat(result.getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.getMemberPwd()).isEqualTo(member.getMemberPwd());
        assertThat(result.getAuthorities()).hasSize(1);
    }

    @DisplayName("인증받은 회원정보 불러오기 실패 -> 멤버가 없음")
    @Test
    void findAuthMember_fail_notFoundMember() {
        entityManager.persist(member);
        entityManager.persist(authority);
        entityManager.persist(memberAuthority);

        assertThatThrownBy(
                () -> memberRepository.findByAuthMemberInfo(1234L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("회원번호로 등급번호 조회 성공 테스트")
    void findTierNoByMemberNoTest() {
        entityManager.persist(bookPubTier);
        entityManager.persist(member);

        Integer result = memberRepository.findTierNoByMemberNo(member.getMemberNo());

        assertThat(result).isEqualTo(bookPubTier.getTierNo());
    }

    @Test
    @DisplayName("회원 닉네임으로 회원 조회 성공 테스트")
    void findMemberByMemberNicknameTest() {
        entityManager.persist(member);

        Optional<Member> result = memberRepository.findMemberByMemberNickname(member.getMemberNickname());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get().getMemberNickname()).isEqualTo(member.getMemberNickname());
        assertThat(result.get().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    @DisplayName("회원 닉네임으로 회원 리스트 조회 성공 테스트")
    void findMembersListByNickNameTest() {
        entityManager.persist(member);
        Pageable pageable = PageRequest.of(0, 10);

        Page<MemberResponseDto> result =
                memberRepository.findMembersListByNickName(pageable, member.getMemberNickname());

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.getContent().get(0).getNickname()).isEqualTo(member.getMemberNickname());
        assertThat(result.getContent().get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getContent().get(0).getEmail()).isEqualTo(member.getMemberEmail());
    }

    @Test
    @DisplayName("회원 아이디로 회원 리스트 조회 성공 테스트")
    void findMembersListById() {
        entityManager.persist(member);
        Pageable pageable = PageRequest.of(0, 10);

        Page<MemberResponseDto> result =
                memberRepository.findMembersListById(pageable, member.getMemberId());

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.getContent().get(0).getNickname()).isEqualTo(member.getMemberNickname());
        assertThat(result.getContent().get(0).getMemberId()).isEqualTo(member.getMemberId());
        assertThat(result.getContent().get(0).getEmail()).isEqualTo(member.getMemberEmail());
    }
}