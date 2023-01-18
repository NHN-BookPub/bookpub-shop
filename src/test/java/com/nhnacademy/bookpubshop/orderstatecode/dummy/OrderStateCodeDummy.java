package com.nhnacademy.bookpubshop.orderstatecode.dummy;

import static com.nhnacademy.bookpubshop.state.OrderState.COMPLETE_PAYMENT;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;

/**
 * 주문상태코드 더미클래스입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
public class OrderStateCodeDummy {

    public static OrderStateCode dummy() {
        return new OrderStateCode(
                null,
                COMPLETE_PAYMENT.getName(),
                COMPLETE_PAYMENT.isUsed(),
                "test_code_info"
        );
    }

    public static OrderStateCode dummy(Integer no) {
        return new OrderStateCode(
                no,
                COMPLETE_PAYMENT.getName(),
                COMPLETE_PAYMENT.isUsed(),
                "test_code_info"
        );
    }

}
