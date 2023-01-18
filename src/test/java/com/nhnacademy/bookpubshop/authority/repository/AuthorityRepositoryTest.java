package com.nhnacademy.bookpubshop.authority.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.authority.dummy.AuthorityDummy;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 권한테이블 Repo Test
 *
 * @author : 유호철
 * @since : 1.0
 **/
@DataJpaTest
class AuthorityRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorityRepository authorityRepository;

    Authority authority;

    @BeforeEach
    void setUp() {
        authority = AuthorityDummy.dummy();
    }

    @DisplayName("권한이 제대로 생성되어있는지 대한 save 테스트")
    @Test
    void authoritySaveTest() {
        Authority persist = entityManager.persist(authority);
        Optional<Authority> result = authorityRepository.findById(persist.getAuthorityNo());

        assertThat(result).isPresent();
        assertThat(result.get().getAuthorityNo()).isEqualTo(persist.getAuthorityNo());
        assertThat(result.get().getAuthorityName()).isEqualTo(persist.getAuthorityName());
    }
}