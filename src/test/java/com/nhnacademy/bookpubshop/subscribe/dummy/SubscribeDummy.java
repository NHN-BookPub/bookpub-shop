package com.nhnacademy.bookpubshop.subscribe.dummy;

import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeProductRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeProductListDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.util.List;
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
                .subscribePrice(1000L)
                .subscribeName("name")
                .salesPrice(100L)
                .salesRate(10)
                .renewed(false)
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

    public static GetSubscribeResponseDto responseDummy() {
        return new GetSubscribeResponseDto(1L, "name", 1000L, 100L,
                10, 0L, false, false, "image");
    }

    public static ModifySubscribeRequestDto modifyDummy() {
        ModifySubscribeRequestDto dto = new ModifySubscribeRequestDto();
        ReflectionTestUtils.setField(dto, "name", "이름");
        ReflectionTestUtils.setField(dto, "salePrice", 1000L);
        ReflectionTestUtils.setField(dto, "price", 1000L);
        ReflectionTestUtils.setField(dto, "saleRate", 0);
        ReflectionTestUtils.setField(dto, "renewed", false);
        return dto;
    }

    public static GetSubscribeDetailResponseDto detailDummy() {
        GetSubscribeDetailResponseDto dto = new GetSubscribeDetailResponseDto();
        ReflectionTestUtils.setField(dto, "subscribeNo", 1L);
        ReflectionTestUtils.setField(dto, "subscribeName", "name");
        ReflectionTestUtils.setField(dto, "price", 1000L);
        ReflectionTestUtils.setField(dto, "salePrice", 100L);
        ReflectionTestUtils.setField(dto, "salesRate", 10);
        ReflectionTestUtils.setField(dto, "viewCnt", 1L);
        ReflectionTestUtils.setField(dto, "deleted", true);
        ReflectionTestUtils.setField(dto, "renewed", true);
        ReflectionTestUtils.setField(dto, "imagePath", "path");
        ReflectionTestUtils.setField(dto, "productLists", List.of(listDto()));

        return dto;
    }

    public static GetSubscribeProductListDto listDto() {
        return new GetSubscribeProductListDto(1L, "title", "filePath");
    }

    public static CreateSubscribeProductRequestDto productListDummy(){
        CreateSubscribeProductRequestDto dto = new CreateSubscribeProductRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", List.of(1L));
        return dto;
    }

}
