package com.nhnacademy.bookpubshop.tier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 회원 등급 레포지토리 테스트.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class BookPubTierRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TierRepository tierRepository;

    BookPubTier bookPubTier;

    @BeforeEach
    void setUp() {
        bookPubTier = TierDummy.dummy();
    }

    @Test
    @DisplayName("회원 등급 레포지토리 저장 테스트")
    void TierSaveTest() {
        BookPubTier persist = entityManager.persist(bookPubTier);

        Optional<BookPubTier> findTier = tierRepository.findById(persist.getTierNo());

        assertThat(findTier).isPresent();
        assertThat(findTier.get().getTierName()).isEqualTo("tier");
    }
}