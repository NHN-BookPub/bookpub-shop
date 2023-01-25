package com.nhnacademy.bookpubshop.token.util;

import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 토큰 사용 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Component
@Slf4j
public class JwtUtil {
    public static final String AUTH_HEADER = "Authorization";
    public static final String ACCESS_TOKEN = "access-token";

    private static final Base64.Decoder decoder = Base64.getUrlDecoder();

    private JwtUtil() {
    }

    /**
     * 토큰을 복호화해주는 메소드.
     *
     * @param jwt accessToken 정보.
     * @return 복호화 된 정보를 리턴해준다.
     */
    public static String decodeJwt(String jwt) {
        String payload = jwt.split("\\.")[1];

        return new String(decoder.decode(payload));
    }
}
