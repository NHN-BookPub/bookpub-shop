package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.dto.response.LoginMemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 을 사용하기위한 Repo 인터페이스.
 *
 * @author : 유호철
 * @since : 1.0
 **/

@NoRepositoryBean
public interface MemberCustomRepository {
    Optional<MemberDetailResponseDto> findByMemberDetails(Long meberNo);

    Page<MemberResponseDto> findMembers(Pageable pageable);

    LoginMemberResponseDto findByMemberLoginInfo(String id, String pwd);
}
