package com.nhnacademy.bookpubshop.member.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * member Request DTO개체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberSignupResponse implements Serializable {
    @NotBlank
    @Size(min = 2, max = 200)
    private String name;

    @NotBlank
    @Size(min = 2, max = 8)
    private String nickname;

    @NotBlank
    @Size(min = 6, max = 6)
    private String birth;

    @NotBlank
    @Size(min = 2, max = 2)
    private String gender;

    @NotBlank
    @Size(min = 5, max = 20)
    private String memberId;

    @NotBlank
    @Size(min = 8, max = 20)
    private String pwd;

    @NotBlank
    @Size(min = 11, max = 11)
    private String phone;

    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String detailAddress;
}
