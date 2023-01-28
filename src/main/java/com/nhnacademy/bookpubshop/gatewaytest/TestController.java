package com.nhnacademy.bookpubshop.gatewaytest;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
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
public class TestController
{
    @GetMapping("/test")
    public String path( @RequestHeader HttpHeaders headers) {
        Map<String, String> map = headers.toSingleValueMap();
        log.warn(map.toString());

        return map.toString();
    }
}
