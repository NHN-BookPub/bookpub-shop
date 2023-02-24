package com.nhnacademy.bookpubshop.member.service.impl;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.address.exception.AddressNotFoundException;
import com.nhnacademy.bookpubshop.address.repository.AddressRepository;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.authority.repository.AuthorityRepository;
import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberEmailRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberNicknameRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPasswordRequest;
import com.nhnacademy.bookpubshop.member.dto.request.ModifyMemberPhoneRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.OauthMemberCreateRequestDto;
import com.nhnacademy.bookpubshop.member.dto.request.SignupDto;
import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberAuthResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberPasswordResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.SignUpMemberResponseDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.event.SignupEvent;
import com.nhnacademy.bookpubshop.member.exception.AuthorityNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.IdAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.exception.NicknameAlreadyExistsException;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.member.service.MemberService;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.tier.exception.TierNotFoundException;
import com.nhnacademy.bookpubshop.tier.repository.TierRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 멤버 서비스 구현.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 **/
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TierRepository tierRepository;
    private final AuthorityRepository authorityRepository;
    private final AddressRepository addressRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final String TIER_NAME = "BASIC";
    private static final String AUTHORITY_NAME = "ROLE_MEMBER";

    /**
     * {@inheritDoc}
     *
     * @throws TierNotFoundException 등급이 없을때 나오는 에러.
     */
    @Transactional
    @Override
    public SignUpMemberResponseDto signup(SignupDto signupDto) {
        duplicateCheck(signupDto);
        Member member = signupDto.createMember(defaultTier());

        updateMemberAuthority(defaultAuthority(), member);
        updateMemberAddress(member, true, signupDto.getAddress(), signupDto.getDetailAddress());

        if (isOauthSignup(signupDto)) {
            member.oauthMember();
        }

        Member saveMember = memberRepository.save(member);
        eventPublisher.publishEvent(new SignupEvent(saveMember));

        return new SignUpMemberResponseDto(
                saveMember.getMemberId(),
                saveMember.getMemberNickname(),
                saveMember.getMemberEmail(),
                saveMember.getTier().getTierName()
        );
    }

    /**
     * oauth로 회원가입 한 유저인지 아닌지 판별하는 메소드.
     *
     * @param signupDto 회원가입 정보.
     * @return true, false.
     */
    private static boolean isOauthSignup(SignupDto signupDto) {
        return signupDto instanceof OauthMemberCreateRequestDto;
    }

    /**
     * 회원의 권한을 기본 권한으로 지정해주는 메소드.
     *
     * @param authority 권한.
     * @param member    회원.
     */
    private static void updateMemberAuthority(Authority authority, Member member) {
        member.addMemberAuthority(new MemberAuthority(
                new MemberAuthority.Pk(member.getMemberNo(), authority.getAuthorityNo()),
                member,
                authority)
        );
    }

    /**
     * 회원의 등급을 기본 등급으로 지정해주는 메소드.
     *
     * @param member        저장하려는 정보.
     * @param base          기준주소지.
     * @param roadAddress   회원가입할때 입력받은 도로명주소 정보 dto.
     * @param detailAddress 회원가입할때 입력받은 상세주소.
     */
    private static void updateMemberAddress(Member member, boolean base,
                                            String roadAddress, String detailAddress) {
        member.getMemberAddress().add(Address.builder()
                .addressMemberBased(base)
                .roadAddress(roadAddress)
                .addressDetail(detailAddress)
                .member(member)
                .build());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NicknameAlreadyExistsException 닉네임이 이미 존재할 때 나오는 에러.
     */
    @Transactional
    @Override
    public void modifyMemberNickName(Long memberNo, ModifyMemberNicknameRequestDto requestDto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        if (!Objects.equals(member.getMemberNickname(), requestDto.getNickname())
                && memberRepository.existsByMemberNickname(requestDto.getNickname())) {
            throw new NicknameAlreadyExistsException(requestDto.getNickname());
        }

        member.modifyNickname(requestDto.getNickname());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyMemberEmail(Long memberNo, ModifyMemberEmailRequestDto requestDto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        if (!Objects.equals(member.getMemberEmail(), requestDto.getEmail())) {
            member.modifyEmail(requestDto.getEmail());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버를 찾지못함.
     */
    @Override
    public MemberDetailResponseDto getMemberDetails(Long memberNo) {
        return memberRepository.findByMemberDetails(memberNo)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberResponseDto> getMembers(Pageable pageable) {
        return memberRepository.findMembers(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 없을때 나오는 에러.
     */
    @Transactional
    @Override
    public void blockMember(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.memberBlock();
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 없을때 나오는 에러.
     */
    @Transactional
    @Override
    public void deleteMember(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        member.memberDelete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginMemberResponseDto loginMember(String loginId) {
        return memberRepository.findByMemberLoginInfo(loginId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean idDuplicateCheck(String id) {
        return memberRepository.existsByMemberId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean nickNameDuplicateCheck(String nickName) {
        return memberRepository.existsByMemberNickname(nickName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberPasswordResponseDto getMemberPwd(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        return new MemberPasswordResponseDto(member);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberAuthResponseDto authMemberInfo(Long memberNo) {
        return memberRepository.findByAuthMemberInfo(memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOauthMember(String email) {
        return memberRepository.existsByMemberId(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTierByMemberNo(Long memberNo) {
        return memberRepository.findTierNoByMemberNo(memberNo);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberResponseDto> getMembersByNickName(Pageable pageable, String search) {
        return memberRepository.findMembersListByNickName(pageable, search);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberResponseDto> getMembersById(Pageable pageable, String search) {
        return memberRepository.findMembersListById(pageable, search);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyMemberName(Long memberNo, ModifyMemberNameRequestDto dto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        member.modifyName(dto.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyMemberPhone(Long memberNo, ModifyMemberPhoneRequestDto dto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        member.modifyPhone(dto.getPhone());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyMemberPassword(Long memberNo, ModifyMemberPasswordRequest password) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        member.modifyPassword(password.getPassword());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberTierStatisticsResponseDto> getTierStatistics() {
        return memberRepository.memberTierStatistics();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MemberStatisticsResponseDto getMemberStatistics() {
        return memberRepository.memberStatistics();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void modifyMemberBaseAddress(Long memberNo, Long addressNo) {
        Address baseAddress = addressRepository.findByMemberBaseAddress(memberNo)
                .orElseThrow(AddressNotFoundException::new);

        Address address = addressRepository.findByMemberExchangeAddress(memberNo, addressNo)
                .orElseThrow(AddressNotFoundException::new);

        baseAddress.modifyAddressBase(false);
        address.modifyAddressBase(true);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addMemberAddress(Long memberNo, CreateAddressRequestDto requestDto) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);

        if (member.getMemberAddress().size() < 10) {
            updateMemberAddress(
                    member,
                    false,
                    requestDto.getAddress(),
                    requestDto.getAddressDetail());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteMemberAddress(Long memberNo, Long addressNo) {
        Address address = addressRepository.findByMemberExchangeAddress(memberNo, addressNo)
                .orElseThrow(AddressNotFoundException::new);
        addressRepository.delete(address);
    }

    /**
     * 아이디, 닉네임 중복체크 메소드입니다.
     *
     * @param member 회원가입하려는 멤버.
     */
    private void duplicateCheck(SignupDto member) {
        if (memberRepository.existsByMemberNickname(member.getNickname())) {
            throw new NicknameAlreadyExistsException(member.getNickname());
        }

        if (memberRepository.existsByMemberId(member.getMemberId())) {
            throw new IdAlreadyExistsException(member.getMemberId());
        }
    }

    /**
     * 기본 등급을 불러오는 메소드.
     *
     * @return tier.
     */
    private BookPubTier defaultTier() {
        return tierRepository.findByTierName(TIER_NAME)
                .orElseThrow(TierNotFoundException::new);
    }

    /**
     * 기본 권한을 불러오는 메소드.
     *
     * @return authority.
     */
    private Authority defaultAuthority() {
        return authorityRepository.findByAuthorityName(AUTHORITY_NAME)
                .orElseThrow(() -> new AuthorityNotFoundException(AUTHORITY_NAME));
    }

}
