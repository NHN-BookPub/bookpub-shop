package com.nhnacademy.bookpubshop.author.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.author.entity.Author;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 저자 레포지토리 테스트
 *
 * @author : 여운석
 * @since : 1.0
 **/
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AuthorRepository authorRepository;

    Author author;

    @BeforeEach
    void setUp() {
        author = new Author(null, "사람");
    }

    @Test
    @DisplayName("저자 save 테스트")
    void memberSaveTest() {
        entityManager.persist(author);
        entityManager.clear();

        Optional<Author> author = authorRepository.findById(1);

        assertThat(author).isPresent();
        assertThat(author.get().getAuthorNo()).isEqualTo(1);
        assertThat(author.get().getAuthorName()).isEqualTo("사람");

    }

}