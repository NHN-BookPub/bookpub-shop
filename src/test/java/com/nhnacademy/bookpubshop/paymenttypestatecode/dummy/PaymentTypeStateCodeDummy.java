package com.nhnacademy.bookpubshop.paymenttypestatecode.dummy;

import com.nhnacademy.bookpubshop.paymenttypestatecode.entity.PaymentTypeStateCode;

/**
 * 결제유형상태 더미클래스.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class PaymentTypeStateCodeDummy {

    public static PaymentTypeStateCode dummy() {
        return new PaymentTypeStateCode(
                null,
                "test_code_name",
                true,
                "test_code_info"
        );
    }

}
