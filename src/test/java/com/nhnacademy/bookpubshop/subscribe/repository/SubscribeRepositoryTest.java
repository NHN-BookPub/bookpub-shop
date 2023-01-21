package com.nhnacademy.bookpubshop.subscribe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
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
        subscribe = SubscribeDummy.dummy();
    }

    @Test
    @DisplayName("구독 save 테스트")
    void memberSaveTest() {
        Subscribe persist = entityManager.persist(subscribe);

        Optional<Subscribe> subscribe = subscribeRepository.findById(persist.getSubscribeNo());

        assertThat(subscribe).isPresent();
        assertThat(subscribe.get().getSubscribeNo()).isEqualTo(persist.getSubscribeNo());
        assertThat(subscribe.get().getSubscribeName()).isEqualTo(persist.getSubscribeName());
        assertThat(subscribe.get().getSubscribePrice()).isEqualTo(persist.getSubscribePrice());
        assertThat(subscribe.get().isSubscribeDeleted()).isEqualTo(persist.isSubscribeDeleted());
        assertThat(subscribe.get().isSubscribeRenewed()).isEqualTo(persist.isSubscribeRenewed());
        assertThat(subscribe.get().getSalesPrice()).isEqualTo(persist.getSalesPrice());
        assertThat(subscribe.get().getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(subscribe.get().getViewCount()).isEqualTo(persist.getViewCount());
    }
}