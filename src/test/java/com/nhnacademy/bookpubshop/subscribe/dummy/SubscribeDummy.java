package com.nhnacademy.bookpubshop.subscribe.dummy;

import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 구독의 더미 클래스입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class SubscribeDummy {
    public static Subscribe dummy() {
        return Subscribe.builder()
                .subscribeName("hi")
                .subscribePrice(5000L)
                .salesRate(10)
                .viewCount(0L)
                .salesPrice(5000L)
                .build();
    }

    public static CreateSubscribeRequestDto createDummy() {
        CreateSubscribeRequestDto dto = new CreateSubscribeRequestDto();
        ReflectionTestUtils.setField(dto, "name", "이름");
        ReflectionTestUtils.setField(dto, "salePrice", 1000L);
        ReflectionTestUtils.setField(dto, "price", 1000L);
        ReflectionTestUtils.setField(dto, "salesRate", 0);
        ReflectionTestUtils.setField(dto, "renewed", true);
        return dto;

    }

    public static GetSubscribeResponseDto responseDummy(){
        return new GetSubscribeResponseDto(1L, "name", 1000L, 100L,
                10, 0L, false, false);
    }

    public static ModifySubscribeRequestDto modifyDummy(){
        ModifySubscribeRequestDto dto = new ModifySubscribeRequestDto();
        ReflectionTestUtils.setField(dto, "name", "이름");
        ReflectionTestUtils.setField(dto, "salePrice", 1000L);
        ReflectionTestUtils.setField(dto, "price", 1000L);
        ReflectionTestUtils.setField(dto, "saleRate", 0);
        ReflectionTestUtils.setField(dto, "renewed", false);
        ReflectionTestUtils.setField(dto, "deleted", false);
        return dto;
    }
}
