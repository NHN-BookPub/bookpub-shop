package com.nhnacademy.bookpubshop.filemanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오브젝트 스토리지 접근을 위한 토큰 요청 정보를 담을 Dto.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@Getter
@NoArgsConstructor
public class TokenRequest {
    private Auth auth;

    @Getter
    @AllArgsConstructor
    public static class Auth {
        private String tenantId;
        private PasswordCredentials passwordCredentials;
    }

    @Getter
    @AllArgsConstructor
    public static class PasswordCredentials {
        private String username;
        private String password;
    }

    public TokenRequest(String tenantId, String username, String password) {
        this.auth = new Auth(tenantId, new PasswordCredentials(username, password));
    }

}