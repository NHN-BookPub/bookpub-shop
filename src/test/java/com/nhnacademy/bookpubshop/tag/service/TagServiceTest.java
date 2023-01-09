package com.nhnacademy.bookpubshop.tag.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.nhnacademy.bookpubshop.tag.dto.request.AddTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.request.ModifyTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.dummy.TagDummy;
import com.nhnacademy.bookpubshop.tag.entity.Tag;
import com.nhnacademy.bookpubshop.tag.exception.TagNameDuplicatedException;
import com.nhnacademy.bookpubshop.tag.exception.TagNotFoundException;
import com.nhnacademy.bookpubshop.tag.repository.TagRepository;
import com.nhnacademy.bookpubshop.tag.service.impl.TagServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * TagService 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/

@ExtendWith(SpringExtension.class)
@Import(TagServiceImpl.class)
class TagServiceTest {

    @Autowired
    TagService tagService;

    @MockBean
    TagRepository tagRepository;

    ArgumentCaptor<Tag> captor;

    Tag tagDummy;
    AddTagRequestDto addTagRequestDto;
    ModifyTagRequestDto modifyTagRequestDto;

    @BeforeEach
    void setUp() {
        tagDummy = TagDummy.dummy();
        addTagRequestDto = new AddTagRequestDto();
        modifyTagRequestDto = new ModifyTagRequestDto();
        captor = ArgumentCaptor.forClass(Tag.class);
    }

    @Test
    @DisplayName("단건 태그 조회 테스트")
    void getTagSuccess_Test() {
        // given
        GetTagResponseDto getTagResponseDto = new GetTagResponseDto(
                tagDummy.getTagNo(), tagDummy.getTagName(), tagDummy.getColorCode());

        // when
        when(tagRepository.findTag(anyInt())).thenReturn(Optional.of(getTagResponseDto));

        // then
        tagService.getTag(anyInt());
        verify(tagRepository, times(1))
                .findTag(anyInt());
    }

    @Test
    @DisplayName("태그 단건 조회 실패 하는 경우")
    void getTagFail_Test() {
        // when
        when(tagRepository.findTag(anyInt())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tagService.getTag(1))
                .isInstanceOf(TagNotFoundException.class)
                .hasMessageContaining("없는 태그 입니다.");
    }

    @Test
    @DisplayName("태그 리스트 조회 테스트")
    void getTagsSuccess_Test() {
        // given
        GetTagResponseDto getTagResponseDto = new GetTagResponseDto(
                tagDummy.getTagNo(), tagDummy.getTagName(), tagDummy.getColorCode());

        // when
        when(tagRepository.findTags()).thenReturn(List.of(getTagResponseDto));

        // then
        tagService.getTags();
        verify(tagRepository, times(1))
                .findTags();
    }

    @Test
    @DisplayName("태그 등록 성공 경우")
    void addTagSuccess_Test() {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "addTagName", tagDummy.getTagName());
        ReflectionTestUtils.setField(addTagRequestDto, "addColorCode", tagDummy.getColorCode());

        // when
        when(tagRepository.existsByTagName(anyString())).thenReturn(false);

        // then
        tagService.addTag(addTagRequestDto);

        verify(tagRepository, times(1)).existsByTagName(anyString());
        verify(tagRepository, times(1)).save(captor.capture());
    }

    @Test
    @DisplayName("태그 이름이 겹쳐서 등록이 실패하는 경우")
    void addTagFail_Test() {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "addTagName", "비추");
        ReflectionTestUtils.setField(addTagRequestDto, "addColorCode", "#FFFFFF");

        // when
        when(tagRepository.existsByTagName(anyString())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tagService.addTag(addTagRequestDto))
                .isInstanceOf(TagNameDuplicatedException.class)
                .hasMessageContaining(" 태그 이름이 중복입니다.");
    }

    @Test
    @DisplayName("태그 정보 수정 성공 테스트")
    void modifyTagSuccess_Test() {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName", "변경 태그 이름");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#000000");

        // when
        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tagDummy));
        when(tagRepository.existsByTagName(anyString())).thenReturn(false);

        // then
        tagService.modifyTagInformation(modifyTagRequestDto);

        verify(tagRepository, times(1))
                .findById(anyInt());
        verify(tagRepository, times(1))
                .existsByTagName(modifyTagRequestDto.getModifyTagName());
    }

    @Test
    @DisplayName("태그 정보 수정 실패 테스트(태그 번호가 없는 경우)")
    void modifyTagFail_NotFound_TagNo_Test() {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName", "변경 태그 이름");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#000000");

        // then
        assertThatThrownBy(() -> tagService.modifyTagInformation(modifyTagRequestDto))
                .isInstanceOf(TagNotFoundException.class)
                .hasMessageContaining("없는 태그 입니다.");
    }

    @Test
    @DisplayName("태그 정보 수정 실패 테스트(태그 이름이 중복인 경우)")
    void modifyTagFail_Duplicated_TagName_Test() {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName", "변경 태그 이름");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#000000");

        // when
        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tagDummy));
        when(tagRepository.existsByTagName(anyString())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tagService.modifyTagInformation(modifyTagRequestDto))
                .isInstanceOf(TagNameDuplicatedException.class)
                .hasMessageContaining("이름이 중복입니다.");
    }

    @Test
    @DisplayName("태그 삭제 성공 테스트")
    void deleteTagSuccess_Test() {
        // when
        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tagDummy));
        when(tagRepository.existsByTagName(anyString())).thenReturn(false);

        // then
        tagService.deleteTagByTagNumber(anyInt());

        verify(tagRepository, times(1)).findById(anyInt());
        verify(tagRepository, times(1)).delete(tagDummy);
    }

    @Test
    @DisplayName("태그 삭제 실패 테스트(태그 번호가 없는 경우)")
    void deleteTagFail_Test() {
        // when
        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> tagService.deleteTagByTagNumber(1))
                .isInstanceOf(TagNotFoundException.class)
                .hasMessageContaining("없는 태그 입니다.");
    }
}

