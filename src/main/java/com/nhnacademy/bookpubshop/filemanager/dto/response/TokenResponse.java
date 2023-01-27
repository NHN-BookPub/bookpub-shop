package com.nhnacademy.bookpubshop.filemanager.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
public class TokenResponse {

    private Access access;

    @Getter
    @NoArgsConstructor
    public static class Access {
        Token token;
    }

    @Getter
    @NoArgsConstructor
    public static class Token {
        LocalDateTime expires;
        String id;
    }
}
