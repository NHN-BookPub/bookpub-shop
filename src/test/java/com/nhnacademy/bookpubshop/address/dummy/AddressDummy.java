package com.nhnacademy.bookpubshop.address.dummy;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.member.dto.request.CreateAddressRequestDto;
import com.nhnacademy.bookpubshop.member.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 주소 더미 클래스
 * 테스트시 해당 클래스를 쉽게 생성하기 위한 클래스 입니다.
 * 동료들과 협력하여 코딩할 때 유용합니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class AddressDummy {
    public static Address dummy(Member member) {
        return Address.builder()
                .member(member)
                .addressMemberBased(true)
                .roadAddress("도로명 주소")
                .addressDetail("상세주소")
                .build();
    }

    public static CreateAddressRequestDto createAddressDtoDummy(){
        CreateAddressRequestDto dto = new CreateAddressRequestDto();
        ReflectionTestUtils.setField(dto, "address", "address");
        ReflectionTestUtils.setField(dto,"addressDetail","addressDetail");

        return dto;
    }
}