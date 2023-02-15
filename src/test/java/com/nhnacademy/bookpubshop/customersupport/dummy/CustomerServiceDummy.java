package com.nhnacademy.bookpubshop.customersupport.dummy;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;

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
                .member(member)
                .build();
    }

    public static CustomerService dummy(CustomerServiceStateCode customerServiceStateCode,
                                        Member member,
                                        String category) {
        return CustomerService.builder()
                .customerServiceStateCode(customerServiceStateCode)
                .serviceCategory(category)
                .serviceContent("content")
                .serviceTitle("title")
                .member(member)
                .build();
    }
}
