package com.nhnacademy.bookpubshop.address.entity;

import com.nhnacademy.bookpubshop.member.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주소 개체입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Table(name = "address")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_number", nullable = false)
    private Long addressNo;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @Column(name = "address_member_based")
    private boolean addressMemberBased;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "address_detail")
    private String addressDetail;


    /**
     * 주소를 등록하기위한 builder 클래스입니다.
     *
     * @param member              회원정보
     * @param addressMemberBased 주 주소로 사용할 정보인지 조회
     * @param roadAddress         도로명 주소
     * @param addressDetail       상세주소 기입.
     */
    @Builder
    public Address(Member member, boolean addressMemberBased,
                   String roadAddress, String addressDetail) {
        this.member = member;
        this.addressMemberBased = addressMemberBased;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
    }


    /**
     * 기준주소지를 변경하기위한 메서드입니다.
     *
     * @param baseAddress 기준주소지에대한 T/F 값이 들어옵니다.
     */
    public void modifyAddressBase(boolean baseAddress) {
        this.addressMemberBased = baseAddress;
    }
}