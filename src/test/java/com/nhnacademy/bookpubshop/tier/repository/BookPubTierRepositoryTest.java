package com.nhnacademy.bookpubshop.tier.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.tier.dto.response.TierResponseDto;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.util.List;
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
        assertThat(findTier.get().getTierValue()).isEqualTo(bookPubTier.getTierValue());
    }

    @Test
    @DisplayName("회원 등급 단건 조회 테스트")
    void getTierTest() {
        BookPubTier save = tierRepository.save(bookPubTier);

        Optional<TierResponseDto> result = tierRepository.findTier(save.getTierNo());

        assertThat(result).isPresent();
        assertThat(result.get().getTierName()).isEqualTo(save.getTierName());
        assertThat(result.get().getTierNo()).isEqualTo(save.getTierNo());
    }

    @DisplayName("회원 등급 다건 조회")
    @Test
    void getTiersTest() {
        BookPubTier save = tierRepository.save(bookPubTier);

        List<TierResponseDto> result = tierRepository.findTiers();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTierName()).isEqualTo(save.getTierName());
        assertThat(result.get(0).getTierNo()).isEqualTo(save.getTierNo());
    }

    @DisplayName("등급명을 통한 조회")
    @Test
    void getTierByNameTest() {
        BookPubTier save = tierRepository.save(bookPubTier);

        Optional<TierResponseDto> result = tierRepository.findTierName(save.getTierName());

        assertThat(result).isPresent();
        assertThat(result.get().getTierName()).isEqualTo(save.getTierName());
        assertThat(result.get().getTierNo()).isEqualTo(save.getTierNo());
    }
}