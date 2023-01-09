package com.nhnacademy.bookpubshop.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

    Tag tag;

    @BeforeEach
    void setUp() {
        tag = TagDummy.dummy();
    }

    @Test
    @DisplayName(value = "태그(tag) 레포지토리 save 테스트 ")
    void tagSaveTest() {
        entityManager.persist(tag);

        Optional<Tag> optional = tagRepository.findById(tag.getTagNo());

        assertThat(optional).isPresent();
        assertThat(optional.get().getTagNo()).isEqualTo(tag.getTagNo());
        assertThat(optional.get().getTagName()).isEqualTo(tag.getTagName());
        assertThat(optional.get().getColorCode()).isEqualTo(tag.getColorCode());

        entityManager.clear();
    }

    @Test
    @DisplayName(value = "태그이름 존재 여부 테스트")
    void isExitsTag_Test() {
        Tag dummyTag = entityManager.persist(tag);

        boolean exists = tagRepository.existsByTagName(dummyTag.getTagName());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName(value = "태그 이름을 가지고 단건 조회 테스트")
    void findTagByTagName_Test() {
        Tag dummyTag = entityManager.persist(tag);

        Optional<GetTagResponseDto> optional = tagRepository.findTagName(dummyTag.getTagName());

        assertThat(optional).isPresent();
        assertThat(optional.get().getTagNo()).isEqualTo(dummyTag.getTagNo());
        assertThat(optional.get().getTagName()).isEqualTo(dummyTag.getTagName());
        assertThat(optional.get().getColorCode()).isEqualTo(dummyTag.getColorCode());
    }

    @Test
    @DisplayName(value = "태그 번호를 가지고 단건 조회 테스트")
    void findTagByTagNo_Test() {
        Tag dummyTag = entityManager.persist(tag);

        Optional<GetTagResponseDto> optional = tagRepository.findTag(dummyTag.getTagNo());

        assertThat(optional).isPresent();
        assertThat(optional.get().getTagNo()).isEqualTo(dummyTag.getTagNo());
        assertThat(optional.get().getTagName()).isEqualTo(dummyTag.getTagName());
        assertThat(optional.get().getColorCode()).isEqualTo(dummyTag.getColorCode());
    }

    @Test
    @DisplayName(value = "전체 태그 조히 테스트")
    void findAllTags_Test() {
        Tag dummyTag = entityManager.persist(tag);

        List<GetTagResponseDto> tags = tagRepository.findTags();

        assertThat(tags).isNotEmpty();
        assertThat(tags.get(0).getTagNo()).isEqualTo(dummyTag.getTagNo());
        assertThat(tags.get(0).getTagName()).isEqualTo(dummyTag.getTagName());
        assertThat(tags.get(0).getColorCode()).isEqualTo(dummyTag.getColorCode());
    }
}