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
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_number")
    private Long memberNo;

    @ManyToOne
    @JoinColumn(name = "tier_number")
    private Tier tier;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Column(name = "member_gender")
    private String memberGender;

    @Column(name = "member_birth")
    private String memberBirth;

    @Column(name = "member_pwd")
    private String memberPwd;

    @Column(name = "member_phone")
    private String memberPhone;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "member_created_at")
    private LocalDateTime createdAt;

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
}
