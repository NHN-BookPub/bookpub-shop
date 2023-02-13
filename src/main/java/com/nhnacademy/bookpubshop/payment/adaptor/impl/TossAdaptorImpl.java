package com.nhnacademy.bookpubshop.payment.adaptor.impl;

import com.nhnacademy.bookpubshop.config.TossConfig;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.response.TossResponseDto;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 토스와 통신하는 어댑터의 구현체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RequiredArgsConstructor
@Component
@Slf4j
public class TossAdaptorImpl implements TossAdaptor {
    private final TossConfig tossConfig;
    private final RestTemplate restTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public TossResponseDto requestPayment(String paymentKey, String orderId, Long amount) {
        final String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.tosspayments.com")
                .path("/v1/payments/confirm")
                .build().toUriString();

        JSONObject param = new JSONObject();
        param.put("paymentKey", paymentKey);
        param.put("orderId", orderId);
        param.put("amount", amount);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(param, tossVersionMakeHeader()),
                TossResponseDto.class
        ).getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TossResponseDto requestRefund(String paymentKey, String cancelReason) {
        final String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.tosspayments.com")
                .path("/v1/payments/")
                .path(paymentKey)
                .path("/cancel")
                .build().toUriString();

        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(param, tossVersionMakeHeader()),
                TossResponseDto.class
        ).getBody();
    }

    /**
     * 토스 요청에 필요한 header를 만드는 메소드.
     *
     * @return header.
     */
    private HttpHeaders tossVersionMakeHeader() {
        HttpHeaders headers = new HttpHeaders();
        String encodeAuth =
                Base64.getEncoder().encodeToString((tossConfig.getSecret() + ":").getBytes());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(encodeAuth);

        return headers;
    }
}
