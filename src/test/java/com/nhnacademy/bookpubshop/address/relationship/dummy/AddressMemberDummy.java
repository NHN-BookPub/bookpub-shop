package com.nhnacademy.bookpubshop.address.relationship.dummy;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.address.relationship.entity.AddressMember;
import com.nhnacademy.bookpubshop.member.entity.Member;

/**
 * 주소 회원 관계 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AddressMemberDummy {
    public static AddressMember dummy(Member member, Address address) {
        return new AddressMember(
                new AddressMember.Pk(1L, 1),
                member,
                address,
                true
        );
    }
}
