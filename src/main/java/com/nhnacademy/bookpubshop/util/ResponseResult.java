package com.nhnacademy.bookpubshop.util;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 응답의 결과를 받아오는 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Component
@Data
public class ResponseResult<T> {
    private Header header;

    private T responseBody;

    /**
     * 응답 상태를 받아오는 header 클래스.
     */
    @Data
    public static class Header {
        private String resState;
    }
}
