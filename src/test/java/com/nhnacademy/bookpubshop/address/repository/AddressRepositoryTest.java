package com.nhnacademy.bookpubshop.address.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.address.dummy.AddressDummy;
import com.nhnacademy.bookpubshop.address.entity.Address;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 주소 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AddressRepository addressRepository;

    Address address;

    @BeforeEach
    void setUp() {
        address = AddressDummy.dummy();
    }

    @Test
    @DisplayName("주소 등록 테스트")
    void AddressSaveTest() {
        entityManager.persist(address);
        entityManager.clear();

        Optional<Address> findAddress = addressRepository.findById(1);

        assertThat(findAddress).isPresent();
        assertThat(findAddress.get().getAddressZipcode()).isEqualTo("61910");
        assertThat(findAddress.get().getAddressBase().contains("광주광역시")).isTrue();
    }
}