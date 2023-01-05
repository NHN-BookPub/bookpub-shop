package com.nhnacademy.bookpubshop.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.service.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.service.entity.CustomerService;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.tier.entity.Tier;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 고객서비스 repo 테스트를 위한 클래스.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class CustomerServiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerServiceRepository customerServiceRepository;

    CustomerService customerService;
    @BeforeEach
    void setUp() {
        customerService = CustomerServiceDummy.dummy(CustomerServiceStateCodeDummy.dummy(),memberDummy());
    }

    @DisplayName("고객서비스 save 테스트")
    @Test
    void name() {
        entityManager.persist(customerService.getCustomerServiceStateCode());
        CustomerService persist = entityManager.persist(customerService);

        Optional<CustomerService> result = customerServiceRepository.findById(persist.getServiceNo());

        assertThat(result).isPresent();
        assertThat(result.get().getServiceNo()).isEqualTo(persist.getServiceNo());
        assertThat(result.get().getServiceCategory()).isEqualTo(persist.getServiceCategory());
        assertThat(result.get().getMember().getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(result.get().getServiceTitle()).isEqualTo(persist.getServiceTitle());
        assertThat(result.get().getCreatedAt()).isEqualTo(persist.getCreatedAt());
        assertThat(result.get().getServiceContent()).isEqualTo(persist.getServiceContent());

    }

    private Member memberDummy(){
        Member testMember = new Member(null, new Tier(null,"tier"), "test_id", "test_nickname", "test_name", "남", 22, 819, "test_pwd", "01012341234",
                "test@test.com", LocalDateTime.now(), false, false, null, 0L, false);
        entityManager.persist(testMember.getTier());
        return entityManager.persist(testMember);
    }
}