package com.nhnacademy.bookpubshop.filemanager.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오브젝트 스토리지 사용 시에 필요한 토큰을 받아오기 위한 dto.
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
