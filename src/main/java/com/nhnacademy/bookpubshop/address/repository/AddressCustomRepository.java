package com.nhnacademy.bookpubshop.address.repository;

import com.nhnacademy.bookpubshop.address.entity.Address;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 쿼리 Dsl 을 사용하기위한 interface 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@NoRepositoryBean
public interface AddressCustomRepository {
    Optional<Address> findByMemberExchangeAddress(Long memberNo, Long addressNo);

    Optional<Address> findByMemberBaseAddress(Long memberNo);
}
