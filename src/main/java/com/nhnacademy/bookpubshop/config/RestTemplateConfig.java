package com.nhnacademy.bookpubshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
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
    /**
     * RestTemplate 를 빈으로 등록하여 사용.
     *
     * @param clientHttpRequestFactory 통신 시간을 설정한 객체.
     * @return restTemplate 반환. 이 객체로 서버간 통신.
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        restTemplate.getInterceptors().add(null);   // 이후 interceptor 추가하여 사용할 계획이있다면 여기에 추가.
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler()); // 이후 error 핸들링할 일이 있다면 여기에 추가.

        return restTemplate;
    }

    /**
     * factory를 빈으로 등록하여 사용.
     * 추가로 공부 후 더 작성.
     *
     * @return factory -> clientHttpRequest의 연결 시간을 설정한 객체.
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(1000);
        factory.setBufferRequestBody(false);

        return factory;
    }

}
