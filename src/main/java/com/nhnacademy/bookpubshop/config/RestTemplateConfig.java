package com.nhnacademy.bookpubshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * restTemplate 설정 클래스.
 * RestTemplate을 Bean으로 등록하여 사용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Configuration
public class RestTemplateConfig {
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(30000);
        factory.setReadTimeout(100000);
        factory.setBufferRequestBody(false);

        return factory;
    }

    /**
     * 클라이언트와 서버간 요청, 응답하기 위한 RestTemplate 빈 설정.
     *
     * @param clientHttpRequestFactory 클라이언트와 서버간 커넥션 설정 factory class.
     * @return RestTemplate 반환.
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

}