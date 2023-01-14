package com.nhnacademy.bookpubshop.member.repository;

import com.nhnacademy.bookpubshop.member.dto.response.MemberDetailResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberStatisticsResponseDto;
import com.nhnacademy.bookpubshop.member.dto.response.MemberTierStatisticsResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * QueryDsl 을 사용하기위한 Repo 인터페이스.
 *
 * @author : 유호철
 * @since : 1.0
 */
@NoRepositoryBean
public interface MemberCustomRepository {
    /**
     * 멤버에 대한 상세정보를 받는 클래스입니다.
     *
     * @param memberNo 멤버 번호
     * @return 멤버상세정보 반환.
     */
    Optional<MemberDetailResponseDto> findByMemberDetails(Long memberNo);

    /**
     * 멤버에대한 통계를 뽑는 메서드입니다.
     *
     * @return 전체 멤버에대한 통계가 나옵니다.
     */
    MemberStatisticsResponseDto memberStatistics();

    /**
     * 멤버에대한 등급별 통계가 나옵니다.
     *
     * @return 등급별 통계가 리스트로 반환됩니다.
     */
    List<MemberTierStatisticsResponseDto> memberTierStatistics();

    /**
     * 멤버들의 정보를 페이징 객체로반환합니다.
     *
     * @param pageable 페이징값.
     * @return 페이된 멤버들의 정보가 반환됩니다.
     */
    Page<MemberResponseDto> findMembers(Pageable pageable);
}
