package com.nhnacademy.bookpubshop.servicecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * CustomerServiceCode Repo 테스트클래스
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class CustomerServiceStateCodeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CustomerServiceStateCodeRepository customerServiceSateCodeRepository;

    CustomerServiceStateCode customerServiceStateCode;

    @BeforeEach
    void setUp() {
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
    }

    @DisplayName("고객서비스 상태코드 save 테스트")
    @Test
    void CustomerServiceCodeSaveTest() {
        CustomerServiceStateCode persist = entityManager.persist(customerServiceStateCode);

        Optional<CustomerServiceStateCode> result = customerServiceSateCodeRepository
                .findById(persist.getServiceCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getServiceCodeNo()).isEqualTo(persist.getServiceCodeNo());
        assertThat(result.get().getServiceCodeName()).isEqualTo(persist.getServiceCodeName());
        assertThat(result.get().getServiceCodeInfo()).isEqualTo(persist.getServiceCodeInfo());
        assertThat(result.get().isServiceCodeUsed()).isTrue();
    }
}