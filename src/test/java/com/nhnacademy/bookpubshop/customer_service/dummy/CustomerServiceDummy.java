package com.nhnacademy.bookpubshop.customer_service.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.customer_service.entity.CustomerService;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import java.time.LocalDateTime;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class CustomerServiceDummy {

    public static CustomerService dummy(CustomerServiceStateCode customerServiceStateCode,
                                        Member member){
        return CustomerService
                .builder()
                .customerServiceStateCode(customerServiceStateCode)
                .serviceCategory("category")
                .serviceContent("content")
                .serviceTitle("title")
                .createdAt(LocalDateTime.now())
                .member(member)
                .build();
    }
}
