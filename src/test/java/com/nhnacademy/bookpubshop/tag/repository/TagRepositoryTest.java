package com.nhnacademy.bookpubshop.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * 태그(Tag) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName(value = "태그(tag) 레포지토리 save 테스트 ")
    void tagSaveTest() {
        Tag testTag = new Tag(null, "tag-name", "#FFFFFF");
        tagRepository.save(testTag);

        Optional<Tag> optional = tagRepository.findById(testTag.getTagNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getTagNo()).isEqualTo(testTag.getTagNo());
        assertThat(optional.get().getTagName()).isEqualTo(testTag.getTagName());
        assertThat(optional.get().getColorCode()).isEqualTo(testTag.getColorCode());

        entityManager.clear();
    }
}