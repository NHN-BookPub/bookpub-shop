package com.nhnacademy.bookpubshop.authority.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import com.nhnacademy.bookpubshop.authority.entity.Authority;
import com.nhnacademy.bookpubshop.dummy.AuthorityDummy;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
@DataJpaTest
class AuthorityRepositoryTest {
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    TestEntityManager entityManager;

    Authority authority;

    @BeforeEach
    void setUp() {
        authority = AuthorityDummy.dummy();
    }

    @Test
    @DisplayName("권한 레포지토리 저장 테스트")
    void AuthoritySaveTest(){
        entityManager.persist(authority);
        entityManager.clear();

        Optional<Authority> findAuthority = authorityRepository.findById(1);

        assertThat(findAuthority).isPresent();
        assertThat(findAuthority.get().getAuthorityName()).isEqualTo("ROLE_ADMIN");
    }
}