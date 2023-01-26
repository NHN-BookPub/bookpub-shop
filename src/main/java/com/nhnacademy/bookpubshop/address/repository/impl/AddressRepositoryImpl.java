package com.nhnacademy.bookpubshop.address.repository.impl;

import com.nhnacademy.bookpubshop.address.entity.Address;
import com.nhnacademy.bookpubshop.address.entity.QAddress;
import com.nhnacademy.bookpubshop.address.repository.AddressCustomRepository;
import com.nhnacademy.bookpubshop.member.entity.QMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * qeryDsl 을 사용하기위한 클래스입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class AddressRepositoryImpl extends QuerydslRepositorySupport
    implements AddressCustomRepository {
    public AddressRepositoryImpl() {
        super(Address.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Address> findByMemberExchangeAddress(Long memberNo, Long addressNo) {
        QAddress address = QAddress.address;
        QMember member = QMember.member;

        return Optional.ofNullable(from(address)
                .innerJoin(address.member, member)
                .where(member.memberNo.eq(memberNo)
                        .and(address.addressNo.eq(addressNo)))
                .select(address)
                .fetchOne());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Address> findByMemberBaseAddress(Long memberNo){
        QAddress address = QAddress.address;
        QMember member = QMember.member;

        return Optional.ofNullable(
                from(address)
                        .innerJoin(address.member, member)
                        .where(member.memberNo.eq(memberNo)
                                .and(address.addressMemberBased.isTrue()))
                        .select(address)
                        .fetchOne()
        );
    }
}
