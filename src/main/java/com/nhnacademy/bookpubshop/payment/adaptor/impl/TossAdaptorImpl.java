package com.nhnacademy.bookpubshop.payment.adaptor.impl;

import com.nhnacademy.bookpubshop.config.TossConfig;
import com.nhnacademy.bookpubshop.payment.adaptor.TossAdaptor;
import com.nhnacademy.bookpubshop.payment.dto.TossResponseDto;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
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
public class TossAdaptorImpl implements TossAdaptor {
    private final TossConfig tossConfig;
    private final RestTemplate restTemplate;

    @Override
    public TossResponseDto requestPayment(String paymentKey, String orderId, Long amount) {
        final String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.tosspayments.com")
                .path("/v1/payments/confirm")
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        String encodeAuth =
                Base64.getEncoder().encodeToString((tossConfig.getSecret() + ":").getBytes());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(encodeAuth);

        JSONObject param = new JSONObject();
        param.put("paymentKey", paymentKey);
        param.put("orderId", orderId);
        param.put("amount", amount);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(param, headers),
                TossResponseDto.class
        ).getBody();
    }
}
