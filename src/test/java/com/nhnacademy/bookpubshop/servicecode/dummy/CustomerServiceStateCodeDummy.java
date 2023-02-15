package com.nhnacademy.bookpubshop.servicecode.dummy;

import static com.nhnacademy.bookpubshop.state.CustomerServiceState.FAQ;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;

/**
 * Some description here.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class CustomerServiceStateCodeDummy {

    public static CustomerServiceStateCode dummy(){
        return CustomerServiceStateCode
                .builder()
                .serviceCodeName(FAQ.getName())
                .serviceCodeInfo("FAQ")
                .serviceCodeUsed(FAQ.isUsed())
                .build();
    }
}
