package com.nhnacademy.bookpubshop.member.entity;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.base.BaseCreateTimeEntity;
import com.nhnacademy.bookpubshop.member.relationship.entity.MemberAuthority;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<MemberAuthority> memberAuthorities = new HashSet<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Address> memberAddress = new ArrayList<>();

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

    /**
     * 멤버 닉네임을 수정할때 쓰이는 메서드입니다.
     *
     * @param memberNickname 수정할 멤버 닉네임.
     * @author : 유호철
     */
    public void modifyNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    /**
     * 멤버 이메일을 수정할때 쓰이는 메서드입니다.
     *
     * @param memberEmail 수정할 멤버 이메일.
     * @author : 유호철
     */
    public void modifyEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    /**
     * 회원의 이름을 수정할때 쓰이는 메서드입니다.
     *
     * @param name 회원의 이름이 기입됩니다.
     */
    public void modifyName(String name) {
        this.memberName = name;
    }

    /**
     * 회원의 휴대전화 번호가 수정될경우 쓰이는 메서드입니다.
     *
     * @param memberPhone 회원의 휴대폰 정보 기입.
     */
    public void modifyPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    /**
     * 회원이 탈퇴했을경우 사용되는 메서드입니다.
     */
    public void memberDelete() {
        this.memberDeleted = !this.memberDeleted;
    }

    /**
     * 회원을 차단했을경우나 차단을 풀었을경우 사용되는 메서드 입니다.
     */
    public void memberBlock() {
        this.memberBlocked = !this.memberBlocked;
        this.blockedAt = LocalDateTime.now();
    }

    /**
     * 패스워드를 수정하기위한 메서드입니다.
     *
     * @param password the password
     */
    public void modifyPassword(String password) {
        this.memberPwd = password;
    }

    /**
     * 멤버 권한을 추가하기 위한 메소드 입니다.
     *
     * @param memberAuthority 멤버권한.
     */
    public void addMemberAuthority(MemberAuthority memberAuthority) {
        this.memberAuthorities.add(memberAuthority);
    }


    /**
     * oauth멤버 컬럼을 true로 수정합니다.
     */
    public void oauthMember() {
        this.socialJoined = true;
    }

    /**
     * 주문 시 사용 한 포인트 차감.
     *
     * @param usePoint 사용한 포인트.
     */
    public void decreaseMemberPoint(Long usePoint) {
        this.memberPoint -= usePoint;
    }

    /**
     * 구매 확정 시 상품 금액 % 대로 포인트 증가.
     *
     * @param savePoint 적립 포인트.
     */
    public void increaseMemberPoint(Long savePoint) {
        this.memberPoint += savePoint;
    }
}
