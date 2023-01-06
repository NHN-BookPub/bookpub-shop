package com.nhnacademy.bookpubshop.member.entity;

import com.nhnacademy.bookpubshop.tier.entity.Tier;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원개체 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Table(name = "member")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_number", nullable = false, unique = true)
    private Long memberNo;

    @ManyToOne
    @JoinColumn(name = "tier_number", nullable = false)
    private Tier tier;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;

    @Column(name = "member_nickname", nullable = false, unique = true)
    private String memberNickname;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_gender", nullable = false)
    private String memberGender;

    @Column(name = "member_birth_year", nullable = false)
    private Integer memberBirthYear;

    @Column(name = "member_birth_month", nullable = false)
    private Integer memberBirthMonth;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

    @Column(name = "member_phone", nullable = false)
    private String memberPhone;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "member_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "member_deleted", nullable = false)
    private boolean memberDeleted;

    @Column(name = "member_blocked", nullable = false)
    private boolean memberBlocked;

    @Column(name = "member_blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "member_point", nullable = false)
    private Long memberPoint;

    @Column(name = "member_social_joined")
    private boolean socialJoined;

    /**
     * front서버에서 전달해 준 DTO 매핑 생성자.
     * Builder를 이용해 default값이 있는 값은 null값을 넣어준다.
     *
     * @param memberId         사용자 아이디
     * @param memberNickname   사용자 닉네임
     * @param memberName       사용자 이름
     * @param memberGender     사용자 성별
     * @param memberBirthYear  사용자 생년
     * @param memberBirthMonth 사용자 월일
     * @param memberPwd        사용자 비밀번호
     * @param memberPhone      사용자 전화번호
     * @param memberEmail      사용자 이메일
     */
    @Builder
    public Member(Tier tier, String memberId, String memberNickname, String memberName, String memberGender, Integer memberBirthYear, Integer memberBirthMonth, String memberPwd, String memberPhone, String memberEmail) {
        this.tier = tier;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.memberName = memberName;
        this.memberGender = memberGender;
        this.memberBirthYear = memberBirthYear;
        this.memberBirthMonth = memberBirthMonth;
        this.memberPwd = memberPwd;
        this.memberPhone = memberPhone;
        this.memberEmail = memberEmail;
        this.createdAt = LocalDateTime.now();
        this.memberPoint = 0L;
    }
}
