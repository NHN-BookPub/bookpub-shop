package com.nhnacademy.bookpubshop.member.entity;

import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
public class Member extends BaseCreateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_number")
    private Long memberNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tier_number")
    private BookPubTier tier;

    @NotNull
    @Column(name = "member_id", unique = true)
    private String memberId;

    @NotNull
    @Column(name = "member_nickname", unique = true)
    private String memberNickname;

    @NotNull
    @Column(name = "member_name")
    private String memberName;

    @NotNull
    @Column(name = "member_gender")
    private String memberGender;

    @NotNull
    @Column(name = "member_birth_year")
    private Integer memberBirthYear;

    @NotNull
    @Column(name = "member_birth_month")
    private Integer memberBirthMonth;

    @NotNull
    @Column(name = "member_pwd")
    private String memberPwd;

    @NotNull
    @Column(name = "member_phone")
    private String memberPhone;

    @NotNull
    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_deleted")
    private boolean memberDeleted;

    @Column(name = "member_blocked")
    private boolean memberBlocked;

    @Column(name = "member_blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "member_point")
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
    public Member(BookPubTier tier, String memberId, String memberNickname, String memberName,
                  String memberGender, Integer memberBirthYear, Integer memberBirthMonth,
                  String memberPwd, String memberPhone, String memberEmail) {
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
        this.memberPoint = 0L;
    }
}
