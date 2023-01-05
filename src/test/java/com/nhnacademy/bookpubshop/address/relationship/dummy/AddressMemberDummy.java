package com.nhnacademy.bookpubshop.address.relationship.dummy;

import com.nhnacademy.bookpubshop.address.relationship.entity.AddressMember;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import java.time.LocalDateTime;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AddressMemberDummy {
    public static AddressMember dummy() {
        return new AddressMember(
                new AddressMember.Pk(1L, 1),
                MemberDummy.dummy(),
                AddressDummy.dummy(),
                LocalDateTime.now(),
                "109동 102호",
                true
        );
    }
}
