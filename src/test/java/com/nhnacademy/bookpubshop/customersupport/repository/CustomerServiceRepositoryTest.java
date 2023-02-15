package com.nhnacademy.bookpubshop.customersupport.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.customersupport.dto.GetCustomerServiceListResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 고객서비스 repo 테스트를 위한 클래스.
 *
 * @author : 유호철, 여운석
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
    Pageable pageable;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        customerServiceStateCode = CustomerServiceStateCodeDummy.dummy();

        bookPubTier = entityManager.persist(bookPubTier);
        member = entityManager.persist(member);
        customerServiceStateCode = entityManager.persist(customerServiceStateCode);

        customerService = CustomerServiceDummy.dummy(customerServiceStateCode, member, "faqUsing");

        pageable = Pageable.ofSize(10);
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

    @Test
    @DisplayName("고객서비스 전체 조회 성공")
    void getCustomerServices() {
        CustomerService persist = customerServiceRepository.save(customerService);

        //when
        Page<GetCustomerServiceListResponseDto> responses
                = customerServiceRepository.getCustomerServices(pageable);

        assertThat(responses.getContent().get(0).getCustomerServiceNo())
                .isEqualTo(persist.getServiceNo());
        assertThat(responses.getContent().get(0).getServiceContent())
                .isEqualTo(persist.getServiceContent());
        assertThat(responses.getContent().get(0).getServiceCategory())
                .isEqualTo(persist.getServiceCategory());
        assertThat(responses.getContent().get(0).getCreatedAt())
                .isBeforeOrEqualTo(persist.getCreatedAt());
        assertThat(responses.getContent().get(0).getMemberId())
                .isEqualTo(persist.getMember().getMemberId());
    }

    @Test
    void getCustomerServicesByCodeName() {
        CustomerService persist = customerServiceRepository.save(customerService);

        //when
        Page<GetCustomerServiceListResponseDto> responses
                = customerServiceRepository
                .getCustomerServicesByCodeName(customerServiceStateCode
                        .getServiceCodeName(), pageable);

        assertThat(responses.getContent().get(0).getCustomerServiceNo())
                .isEqualTo(persist.getServiceNo());
        assertThat(responses.getContent().get(0).getServiceContent())
                .isEqualTo(persist.getServiceContent());
        assertThat(responses.getContent().get(0).getServiceCategory())
                .isEqualTo(persist.getServiceCategory());
        assertThat(responses.getContent().get(0).getCreatedAt())
                .isBeforeOrEqualTo(persist.getCreatedAt());
        assertThat(responses.getContent().get(0).getMemberId())
                .isEqualTo(persist.getMember().getMemberId());
    }

    @Test
    void getCustomerServicesByCategory() {
        CustomerService persist = customerServiceRepository.save(customerService);

        //when
        Page<GetCustomerServiceListResponseDto> responses
                = customerServiceRepository
                .getCustomerServicesByCategory("faqUsing", pageable);

        assertThat(responses.getContent().get(0).getCustomerServiceNo())
                .isEqualTo(persist.getServiceNo());
        assertThat(responses.getContent().get(0).getServiceContent())
                .isEqualTo(persist.getServiceContent());
        assertThat(responses.getContent().get(0).getServiceCategory())
                .isEqualTo(persist.getServiceCategory());
        assertThat(responses.getContent().get(0).getCreatedAt())
                .isBeforeOrEqualTo(persist.getCreatedAt());
        assertThat(responses.getContent().get(0).getMemberId())
                .isEqualTo(persist.getMember().getMemberId());
    }

    @Test
    void findCustomerServiceByNo() {
        CustomerService persist = customerServiceRepository.save(customerService);

        //when
        Optional<GetCustomerServiceListResponseDto> responseDto =
                customerServiceRepository.findCustomerServiceByNo(persist.getServiceNo());

        assertThat(responseDto.get().getCustomerServiceNo()).isEqualTo(persist.getServiceNo());
        assertThat(responseDto.get().getServiceTitle()).isEqualTo(persist.getServiceTitle());
        assertThat(responseDto.get().getServiceCategory()).isEqualTo(persist.getServiceCategory());
        assertThat(responseDto.get().getMemberId()).isEqualTo(persist.getMember().getMemberId());
        assertThat(responseDto.get().getCreatedAt()).isAfterOrEqualTo(persist.getCreatedAt());

    }
}