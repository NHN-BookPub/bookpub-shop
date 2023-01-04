package com.nhnacademy.bookpubshop.cardstatecode.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.cardstatecode.dummy.CardStateCodeDummy;
import com.nhnacademy.bookpubshop.cardstatecode.entity.CardStateCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 카드결제상태코드 Repo Test
 *
 * @author : 김서현
 * @since : 1.0
 **/
@DataJpaTest
class CardStateCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardStateCodeRepository cardStateCodeRepository;

    CardStateCode cardStateCode;

    @BeforeEach
    void setUp() {
        cardStateCode = CardStateCodeDummy.dummy();
    }

    @Test
    @DisplayName(value = "카드결제상태코드 save 테스트")
    void cardStateCodeSaveTest() {
        entityManager.persist(cardStateCode);

        Optional<CardStateCode> result = cardStateCodeRepository.findById(
                cardStateCode.getCodeNo());

        assertThat(result).isPresent();
        assertThat(result.get().getCodeNo()).isEqualTo(cardStateCode.getCodeNo());
        assertThat(result.get().getCodeName()).isEqualTo(cardStateCode.getCodeName());
        assertThat(result.get().isCodeUsed()).isEqualTo(cardStateCode.isCodeUsed());
        assertThat(result.get().getCodeInfo()).isEqualTo(cardStateCode.getCodeInfo());
    }

}