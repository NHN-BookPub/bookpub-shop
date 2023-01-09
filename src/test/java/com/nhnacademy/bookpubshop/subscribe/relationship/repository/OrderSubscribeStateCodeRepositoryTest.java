package com.nhnacademy.bookpubshop.subscribe.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.state.SubscribeState;
import com.nhnacademy.bookpubshop.subscribe.relationship.entity.OrderSubscribeStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 구독상태코드 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class OrderSubscribeStateCodeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderSubscribeStateCodeRepository orderSubscribeStateCodeRepository;

    OrderSubscribeStateCode stateCode;

    @BeforeEach
    void setUp() {
        stateCode = new OrderSubscribeStateCode(null, SubscribeState.WAITING_SUBSCRIPTION.getName(), SubscribeState.WAITING_SUBSCRIPTION.isUsed(), "구독중입니다.");
    }

    @Test
    @DisplayName("주문구독상태코드 save 테스트")
    void orderSubscribeStateCodeSaveTest() {
        OrderSubscribeStateCode persist = entityManager.persist(stateCode);

        Optional<OrderSubscribeStateCode> orderSubscribeStateCode = orderSubscribeStateCodeRepository.findById(persist.getCodeNo());

        assertThat(orderSubscribeStateCode).isPresent();
        assertThat(orderSubscribeStateCode.get().getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(orderSubscribeStateCode.get().getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(orderSubscribeStateCode.get().getCodeInfo()).isEqualTo(persist.getCodeInfo());
        assertThat(orderSubscribeStateCode.get().isCodeUsed()).isEqualTo(persist.isCodeUsed());
    }
}