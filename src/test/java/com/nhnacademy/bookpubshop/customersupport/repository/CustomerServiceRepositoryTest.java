package com.nhnacademy.bookpubshop.customersupport.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.customersupport.dummy.CustomerServiceDummy;
import com.nhnacademy.bookpubshop.customersupport.entity.CustomerService;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.servicecode.dummy.CustomerServiceStateCodeDummy;
import com.nhnacademy.bookpubshop.servicecode.entity.CustomerServiceStateCode;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
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
    BookPubTier bookPubTier;
    CustomerServiceStateCode customerServiceStateCode;
    Member member;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();
        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member);

        entityManager.persist(bookPubTier);
        entityManager.persist(member);
        entityManager.persist(customerServiceStateCode);
    }

    @DisplayName("고객서비스 save 테스트")
    @Test
    void name() {
        LocalDateTime now = LocalDateTime.now();

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
        assertThat(result.get().getCreatedAt()).isAfter(now);
    }

}