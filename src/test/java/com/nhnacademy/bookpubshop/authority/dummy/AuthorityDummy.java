package com.nhnacademy.bookpubshop.authority.dummy;

import com.nhnacademy.bookpubshop.authority.entity.Authority;

/**
 * 권한 dummy 클래스
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class AuthorityDummy {

    public static Authority dummy(){
        return  Authority.builder()
                .authorityName("member").build();
    }
}
