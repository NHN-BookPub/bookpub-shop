package com.nhnacademy.bookpubshop.subscribe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 구독 레포지토리 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class SubscribeRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    SubscribeRepository subscribeRepository;

    Subscribe subscribe;

    @BeforeEach
    void setUp() {
        subscribe = new Subscribe(null, "좋은생각", 80000L, 100000L,
                20, 100L, false, LocalDateTime.now(), true);
    }

    @Test
    @DisplayName("구독 save 테스트")
    void memberSaveTest() {
        entityManager.persist(subscribe);
        entityManager.clear();

        Optional<Subscribe> subscribe = subscribeRepository.findById(1L);

        assertThat(subscribe).isPresent();
        assertThat(subscribe.get().getSubscribeNo()).isEqualTo(1L);
        assertThat(subscribe.get().getSubscribeName()).isEqualTo("좋은생각");

    }
}