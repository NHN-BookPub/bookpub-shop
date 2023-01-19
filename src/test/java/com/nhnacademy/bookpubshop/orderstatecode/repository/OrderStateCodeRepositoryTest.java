package com.nhnacademy.bookpubshop.orderstatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.orderstatecode.dto.GetOrderStateCodeResponseDto;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 주문상태코드 Repo Test 입니다.
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class OrderStateCodeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderStateCodeRepository orderStateCodeRepository;

    OrderStateCode orderStateCode;

    @BeforeEach
    void setUp() {
        orderStateCode = OrderStateCodeDummy.dummy();
    }

    @Test
    @DisplayName("주문상태코드 save 테스트")
    void orderStateCodeSaveTest() {
        entityManager.persist(orderStateCode);
        entityManager.clear();

        Optional<OrderStateCode> result = orderStateCodeRepository.findById(
                orderStateCode.getCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(orderStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(orderStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(orderStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(orderStateCode.getCodeInfo());
    }

    @Test
    @DisplayName("주문상태 단일 조회 성공")
    void findStateCodeByNo() {
        OrderStateCode persist = entityManager.persist(orderStateCode);

        GetOrderStateCodeResponseDto result = orderStateCodeRepository.findStateCodeByNo(persist.getCodeNo())
                .orElseThrow(NotFoundStateCodeException::new);

        assertThat(result.getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(result.getCodeInfo()).isEqualTo(persist.getCodeInfo());
        assertThat(result.getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(result.isCodeUsed()).isEqualTo(persist.isCodeUsed());
    }

    @Test
    @DisplayName("주문상태 전체 조회 성공")
    void findStateCodes() {
        OrderStateCode persist = entityManager.persist(orderStateCode);

        List<GetOrderStateCodeResponseDto> result = orderStateCodeRepository.findStateCodes();

        assertThat(result.get(0).getCodeName()).isEqualTo(persist.getCodeName());
        assertThat(result.get(0).getCodeNo()).isEqualTo(persist.getCodeNo());
        assertThat(result.get(0).getCodeInfo()).isEqualTo(persist.getCodeInfo());
        assertThat(result.get(0).isCodeUsed()).isEqualTo(persist.isCodeUsed());
    }
}