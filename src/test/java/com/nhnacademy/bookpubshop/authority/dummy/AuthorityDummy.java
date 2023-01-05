package com.nhnacademy.bookpubshop.authority.dummy;

import com.nhnacademy.bookpubshop.authority.entity.Authority;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AuthorityDummy {
    public static Authority dummy() {
        return new Authority(null, "ROLE_ADMIN");
    }
}
