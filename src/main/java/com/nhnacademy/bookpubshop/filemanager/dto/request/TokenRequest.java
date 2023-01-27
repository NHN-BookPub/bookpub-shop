package com.nhnacademy.bookpubshop.filemanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Some description here.
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