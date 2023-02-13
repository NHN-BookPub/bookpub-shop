package com.nhnacademy.bookpubshop.servicecode.repository.impl;

import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.servicecode.entity.QCustomerServiceStateCode;
import com.nhnacademy.bookpubshop.servicecode.repository.CustomerServiceStateCodeRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 고객 서비스 커스텀 레포의 구현체입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class CustomerServiceStateCodeRepositoryImpl extends QuerydslRepositorySupport
        implements CustomerServiceStateCodeRepositoryCustom {
    public CustomerServiceStateCodeRepositoryImpl() {
        super(CustomerServiceStateCode.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<CustomerServiceStateCode>
    getCustomerServiceStateCodeByName(String codeName) {
        QCustomerServiceStateCode customerServiceStateCode = QCustomerServiceStateCode.customerServiceStateCode;

        return Optional.of(
                from(customerServiceStateCode)
                .select(customerServiceStateCode)
                .where(customerServiceStateCode.serviceCodeName.eq(codeName))
                .fetchOne());
    }
}
