package com.nhnacademy.bookpubshop.dummy;

import com.nhnacademy.bookpubshop.address.entity.Address;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AddressDummy {
    public static Address dummy() {
        return new Address(null, "61910", "광주광역시 서구 상무버들로 40번길 14");
    }
}
