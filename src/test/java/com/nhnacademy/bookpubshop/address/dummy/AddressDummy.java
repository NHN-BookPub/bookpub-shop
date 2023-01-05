package com.nhnacademy.bookpubshop.address.dummy;

import com.nhnacademy.bookpubshop.address.entity.Address;

/**
 * 주소 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AddressDummy {
    public static Address dummy() {
        return new Address(null,
                "61910",
                "광주광역시 서구 상무버들로 40번길 14",
                "109동 102호"
        );
    }
}
