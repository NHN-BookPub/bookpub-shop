package com.nhnacademy.bookpubshop.gatewaytest;

import com.nhnacademy.bookpubshop.annotation.MemberAuth;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게이트 웨이와 테스트하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Slf4j
@RestController
public class TestController {
    @MemberAuth
    @GetMapping("/test/{memberNo}")
    public String path(
            @PathVariable(value = "memberNo",required = false) Long memberNo,
            @RequestHeader HttpHeaders headers) {
        log.warn("memberNo : {}", memberNo);
        Map<String, String> map = headers.toSingleValueMap();

        log.warn(map.toString());

        return map.toString();
    }
}
