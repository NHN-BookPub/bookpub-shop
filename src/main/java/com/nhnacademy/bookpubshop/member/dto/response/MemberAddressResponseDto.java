package com.nhnacademy.bookpubshop.member.dto.response;

import com.nhnacademy.bookpubshop.address.entity.Address;
import lombok.Getter;

/**
 * 멤버에대한 주소정보를 반환하는 객체입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@Getter
public class MemberAddressResponseDto {
    private final Long addressNo;
    private final String roadAddress;
    private final String addressDetail;
    private final boolean addressBased;

    /**
     * 회원의 주소정보를 받기위한 생성자입니다.
     *
     * @param address the address
     */
    public MemberAddressResponseDto(Address address) {
        this.addressNo = address.getAddressNo();
        this.roadAddress = address.getRoadAddress();
        this.addressDetail = address.getAddressDetail();
        this.addressBased = address.isAddressMemberBased();
    }
}
