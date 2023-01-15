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
    Optional<MemberDetailResponseDto> findByMemberDetails(Long memberNo);

    Page<MemberResponseDto> findMembers(Pageable pageable);

    /**
     * 로그인 한 멤버의 정보를 불러오는 메소드
     *
     * @param id 입력받은 id
     * @return 로그인 멤버 정보 dto.
     */
    LoginMemberResponseDto findByMemberLoginInfo(String id);
}
