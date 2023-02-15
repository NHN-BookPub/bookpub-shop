package com.nhnacademy.bookpubshop.customersupport.repository.impl;

import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.customersupport.entity.QCustomerService;
import com.nhnacademy.bookpubshop.customersupport.repository.CustomerServiceRepositoryCustom;
import com.nhnacademy.bookpubshop.file.entity.QFile;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import com.nhnacademy.bookpubshop.servicecode.entity.QCustomerServiceStateCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * 쿼리 dsl 사용하기 위한 custom 클래스의 구현체 입니다.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public class CustomerServiceRepositoryImpl extends QuerydslRepositorySupport implements CustomerServiceRepositoryCustom {
    public CustomerServiceRepositoryImpl() {
        super(CustomerService.class);
    }

    private static final String IMAGE = "image";
    QCustomerService customerService = QCustomerService.customerService;
    QCustomerServiceStateCode customerServiceStateCode = QCustomerServiceStateCode.customerServiceStateCode;
    QMember member = QMember.member;
    QFile file = QFile.file;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCustomerServiceListResponseDto> getCustomerServices(Pageable pageable) {
        List<GetCustomerServiceListResponseDto> contents =
                from(customerService)
                        .select(Projections.constructor(
                                GetCustomerServiceListResponseDto.class,
                                customerService.serviceNo,
                                customerServiceStateCode.serviceCodeInfo,
                                customerService.member.memberId,
                                file.filePath.as(IMAGE),
                                customerService.serviceCategory,
                                customerService.serviceTitle,
                                customerService.serviceContent,
                                customerService.createdAt))
                        .innerJoin(member).on(member.memberNo.eq(customerService.member.memberNo))
                        .innerJoin(customerServiceStateCode)
                        .on(customerServiceStateCode.serviceCodeNo
                                .eq(customerService.customerServiceStateCode.serviceCodeNo))
                        .leftJoin(file).on(file.customerService.serviceNo.eq(customerService.serviceNo))
                        .orderBy(customerService.serviceNo.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPQLQuery<Long> count =
                from(customerService)
                        .select(customerService.serviceNo.count());

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCustomerServiceListResponseDto> getCustomerServicesByCodeName(String codeName, Pageable pageable) {
        List<GetCustomerServiceListResponseDto> contents =
                from(customerService)
                        .select(Projections.constructor(
                                GetCustomerServiceListResponseDto.class,
                                customerService.serviceNo,
                                customerServiceStateCode.serviceCodeInfo,
                                member.memberId,
                                file.filePath.as(IMAGE),
                                customerService.serviceCategory,
                                customerService.serviceTitle,
                                customerService.serviceContent,
                                customerService.createdAt))
                        .innerJoin(member).on(member.memberNo.eq(customerService.member.memberNo))
                        .innerJoin(customerServiceStateCode)
                        .on(customerServiceStateCode.serviceCodeNo
                                .eq(customerService.customerServiceStateCode.serviceCodeNo))
                        .leftJoin(file).on(file.customerService.serviceNo.eq(customerService.serviceNo))
                        .where(customerServiceStateCode.serviceCodeName.eq(codeName))
                        .orderBy(customerService.serviceNo.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch();

        JPQLQuery<Long> count = from(customerService)
                .where(customerService.customerServiceStateCode.serviceCodeName.eq(codeName))
                .innerJoin(customerServiceStateCode)
                .on(customerServiceStateCode.serviceCodeNo
                        .eq(customerService.customerServiceStateCode.serviceCodeNo))
                .select(customerService.serviceNo.count());

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetCustomerServiceListResponseDto> getCustomerServicesByCategory(String category, Pageable pageable) {
        List<GetCustomerServiceListResponseDto> contents =
                from(customerService)
                        .select(Projections.constructor(
                                GetCustomerServiceListResponseDto.class,
                                customerService.serviceNo,
                                customerServiceStateCode.serviceCodeInfo,
                                customerService.member.memberId,
                                file.filePath.as(IMAGE),
                                customerService.serviceCategory,
                                customerService.serviceTitle,
                                customerService.serviceContent,
                                customerService.createdAt))
                        .innerJoin(member).on(member.memberNo.eq(customerService.member.memberNo))
                        .innerJoin(customerServiceStateCode)
                        .on(customerServiceStateCode.serviceCodeNo
                                .eq(customerService.customerServiceStateCode.serviceCodeNo))
                        .leftJoin(file).on(file.customerService.serviceNo.eq(customerService.serviceNo))
                        .where(customerService.serviceCategory.eq(category))
                        .orderBy(customerService.serviceNo.desc())
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch();

        JPQLQuery<Long> count = from(customerService)
                .where(customerService.serviceCategory.eq(category))
                .select(customerService.serviceNo.count());

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<GetCustomerServiceListResponseDto> findCustomerServiceByNo(Integer serviceNo) {
        return Optional.of(
                from(customerService)
                .select(Projections.constructor(
                        GetCustomerServiceListResponseDto.class,
                        customerService.serviceNo,
                        customerServiceStateCode.serviceCodeInfo,
                        customerService.member.memberId,
                        file.filePath.as(IMAGE),
                        customerService.serviceCategory,
                        customerService.serviceTitle,
                        customerService.serviceContent,
                        customerService.createdAt))
                .innerJoin(member).on(member.memberNo.eq(customerService.member.memberNo))
                .innerJoin(customerServiceStateCode)
                .on(customerServiceStateCode.serviceCodeNo
                        .eq(customerService.customerServiceStateCode.serviceCodeNo))
                .leftJoin(file).on(file.customerService.serviceNo.eq(customerService.serviceNo))
                .where(customerService.serviceNo.eq(serviceNo))
                .fetchOne());
    }
}
