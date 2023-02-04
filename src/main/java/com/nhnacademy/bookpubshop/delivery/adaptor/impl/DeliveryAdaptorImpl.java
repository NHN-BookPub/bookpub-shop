package com.nhnacademy.bookpubshop.delivery.adaptor.impl;

import com.nhnacademy.bookpubshop.delivery.adaptor.DeliveryAdaptor;
import com.nhnacademy.bookpubshop.delivery.dto.request.CreateDeliveryRequestDto;
import com.nhnacademy.bookpubshop.delivery.dto.response.CreateDeliveryResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * 배송서버로 통신하기위한 Adaptor 입니다
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryAdaptorImpl implements DeliveryAdaptor {
    @Value("${bookpub.delivery-url}")
    private String url;
    private final RestTemplate restTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto dto) {

        String path = url + "/delivery";
        log.warn("path: {}", path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<CreateDeliveryRequestDto> httpEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<CreateDeliveryResponseDto> result = restTemplate.postForEntity(
                path,
                httpEntity,
                CreateDeliveryResponseDto.class
        );
        return result.getBody();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
