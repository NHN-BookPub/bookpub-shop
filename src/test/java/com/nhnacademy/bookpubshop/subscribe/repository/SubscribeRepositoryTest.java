package com.nhnacademy.bookpubshop.subscribe.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy;
import com.nhnacademy.bookpubshop.subscribe.entity.Subscribe;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    File file;

    @BeforeEach
    void setUp() {
        subscribe = SubscribeDummy.dummy();
        file = FileDummy.dummy2(null, null, null, subscribe, null, null);
        subscribe.setFile(file);
    }

    @Test
    @DisplayName("구독 save 테스트")
    void subscribeSaveTest() {
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


    @Test
    @DisplayName("구독 조회 테스트")
    void subscribeInfo() {
        PageRequest page = PageRequest.of(0, 10);
        entityManager.persist(file.getSubscribe());
        entityManager.persist(file);
        Subscribe persist = file.getSubscribe();
        Page<GetSubscribeResponseDto> result = subscribeRepository.getSubscribes(page);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getSubscribeNo()).isEqualTo(persist.getSubscribeNo());
        assertThat(result.getContent().get(0).getSubscribeName()).isEqualTo(persist.getSubscribeName());
        assertThat(result.getContent().get(0).getSalesRate()).isEqualTo(persist.getSalesRate());
        assertThat(result.getContent().get(0).getViewCnt()).isEqualTo(persist.getViewCount());
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(persist.getSubscribePrice());
        assertThat(result.getContent().get(0).getSalePrice()).isEqualTo(persist.getSalesPrice());
        assertThat(result.getContent().get(0).isDeleted()).isEqualTo(persist.isSubscribeDeleted());
        assertThat(result.getContent().get(0).isRenewed()).isEqualTo(persist.isSubscribeRenewed());

    }
}