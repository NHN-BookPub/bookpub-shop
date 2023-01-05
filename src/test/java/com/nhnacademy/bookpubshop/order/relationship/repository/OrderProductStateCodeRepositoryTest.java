package com.nhnacademy.bookpubshop.order.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 주문상품상태코드 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class OrderProductStateCodeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderProductStateCodeRepository orderProductStateCodeRepository;

    OrderProductStateCode stateCode;

    @BeforeEach
    void setUp() {
        stateCode = new OrderProductStateCode(null, OrderProductState.CONFIRMED.getName(), OrderProductState.CONFIRMED.isUsed(), "주문완료되었습니다.");
    }

    @Test
    @DisplayName("주문상품상태코드 save 테스트")
    void memberSaveTest() {
        OrderProductStateCode persist = entityManager.persist(stateCode);

        Optional<OrderProductStateCode> stateCode = orderProductStateCodeRepository.findById(persist.getCodeNo());

        assertThat(stateCode).isPresent();
        assertThat(stateCode.get().getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(stateCode.get().getCodeName()).isEqualTo(OrderProductState.CONFIRMED.getName());

    }
}