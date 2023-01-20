package com.nhnacademy.bookpubshop.order.relationship.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.order.relationship.dto.GetOrderProductStateCodeResponseDto;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import java.util.List;
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
        stateCode = new OrderProductStateCode(null,
                OrderProductState.CONFIRMED.getName(),
                OrderProductState.CONFIRMED.isUsed(),
                "주문완료되었습니다.");
    }

    @Test
    @DisplayName("주문상품상태코드 save 테스트")
    void memberSaveTest() {
        OrderProductStateCode persist = entityManager.persist(stateCode);

        Optional<OrderProductStateCode> stateCode = orderProductStateCodeRepository.findById(persist.getCodeNo());

        assertThat(stateCode).isPresent();
        assertThat(stateCode.get().getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(stateCode.get().getCodeName()).isEqualTo(OrderProductState.CONFIRMED.getName());
        assertThat(stateCode.get().isCodeUsed()).isTrue();
        assertThat(stateCode.get().getCodeInfo()).isEqualTo(persist.getCodeInfo());
    }

    @Test
    @DisplayName("주문상품상태코드 단일 조회 성공")
    void findCodeById() {
        OrderProductStateCode persist = entityManager.persist(stateCode);

        GetOrderProductStateCodeResponseDto result =
                orderProductStateCodeRepository.findCodeById(persist.getCodeNo())
                        .orElseThrow(NotFoundStateCodeException::new);

        assertThat(result.getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(result.getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(result.getCodeInfo()).isEqualTo(persist.getCodeInfo());
        assertThat(result.isCodeUsed()).isEqualTo(persist.isCodeUsed());
    }

    @Test
    @DisplayName("주문상품상태코드 전체 조회 성공")
    void findCodeAll() {
        OrderProductStateCode persist = entityManager.persist(stateCode);

        List<GetOrderProductStateCodeResponseDto> result =
                orderProductStateCodeRepository.findCodeAll();

        assertThat(result.get(0).getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(result.get(0).getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(result.get(0).getCodeInfo()).isEqualTo(persist.getCodeInfo());
        assertThat(result.get(0).isCodeUsed()).isEqualTo(persist.isCodeUsed());
    }
}