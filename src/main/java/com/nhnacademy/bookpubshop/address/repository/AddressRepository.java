package com.nhnacademy.bookpubshop.address.repository;

import com.nhnacademy.bookpubshop.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 주소 Jpa 레포지토리.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface AddressRepository extends JpaRepository<Address, Long>, AddressCustomRepository {

}
