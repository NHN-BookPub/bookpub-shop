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
    /**
     * 회원의 기준주소지로 변경할 주소 알기위한 메서드입니다.
     *
     * @param memberNo 회원번호
     * @param addressNo 변경할 주소번호
     * @return 주소값이 반환됩니다.
     */
    Optional<Address> findByMemberExchangeAddress(Long memberNo, Long addressNo);

    /**
     * 회원의 기준 주소지를 찾아내기위한 메서드입니다.
     *
     * @param memberNo 회원의 번호
     * @return 기준주소지가 반환됩니다.
     */
    Optional<Address> findByMemberBaseAddress(Long memberNo);
}
