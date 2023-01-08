package com.nhnacademy.bookpubshop.tag.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.tag.dto.request.AddTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.request.ModifyTagRequestDto;
import com.nhnacademy.bookpubshop.tag.dto.response.GetTagResponseDto;
import com.nhnacademy.bookpubshop.tag.service.TagService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tag Rest API 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@WebMvcTest(TagController.class)
@Import(ShopAdviceController.class)
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagService tagService;

    ObjectMapper mapper;
    AddTagRequestDto addTagRequestDto;
    ModifyTagRequestDto modifyTagRequestDto;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        addTagRequestDto = new AddTagRequestDto();
        modifyTagRequestDto = new ModifyTagRequestDto();
    }

    @Test
    @DisplayName("단건 태그 조회 api 테스트")
    void get_tagDetailSuccess_test() throws Exception {
        // given
        GetTagResponseDto getTagResponseDto = new GetTagResponseDto(1, "강추", "#FFFFFF");

        // when
        when(tagService.getTag(getTagResponseDto.getTagNo()))
                .thenReturn(getTagResponseDto);

        // then
        mockMvc.perform(get("/api/tags/{tagNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagNo").value(getTagResponseDto.getTagNo()))
                .andExpect(jsonPath("$.tagName").value(getTagResponseDto.getTagName()))
                .andExpect(jsonPath("$.colorCode").value(getTagResponseDto.getColorCode()))
                .andDo(print());

        verify(tagService, times(1))
                .getTag(anyInt());
    }

    @Test
    @DisplayName("태그 리스트 조회 api 테스트")
    void get_tagListSuccess_test() throws Exception {
        // given
        GetTagResponseDto getTagResponseDto = new GetTagResponseDto(1, "강추", "#FFFFFF");

        // when
        when(tagService.getTags())
                .thenReturn(List.of(getTagResponseDto));

        // then
        mockMvc.perform(get("/api/tags")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagNo").value(getTagResponseDto.getTagNo()))
                .andExpect(jsonPath("$[0].tagNo").value(getTagResponseDto.getTagNo()))
                .andExpect(jsonPath("$[0].tagNo").value(getTagResponseDto.getTagNo()))
                .andDo(print());

        verify(tagService, times(1))
                .getTags();
    }

    @Test
    @DisplayName("태그 생성 성공 테스트")
    void add_tagSuccess_test() throws Exception {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "tagName", "강추");
        ReflectionTestUtils.setField(addTagRequestDto, "colorCode", "#E1FFE1");

        // when
        doNothing().when(tagService).addTag(addTagRequestDto);

        // then
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addTagRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(tagService).should().addTag(any(AddTagRequestDto.class));
    }

    @Test
    @DisplayName("태그 생성 실패 테스트 (Validation Exception)")
    void add_tag_fail_validation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "tagName", "태그");
        ReflectionTestUtils.setField(addTagRequestDto, "colorCode", "#PIUYT1");

        // when
        doNothing().when(tagService).addTag(addTagRequestDto);

        // then
        mockMvc.perform(post("/api/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(addTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("지원하지 않는 색상 코드입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("태그 수정 성공 테스트")
    void modify_tag_success_test() throws Exception {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "tagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "tagName", "변경태그");
        ReflectionTestUtils.setField(modifyTagRequestDto, "colorCode", "#47FF9C");

        // when
        doNothing().when(tagService).modifyTagInformation(modifyTagRequestDto);

        // then
        mockMvc.perform(put("/api/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(modifyTagRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(tagService).should().modifyTagInformation(any(ModifyTagRequestDto.class));
    }

    @Test
    @DisplayName("태그 수정 실패 테스트 (Validation Exception)")
    void modify_tag_fail_validation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "tagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "tagName", "태그A");
        ReflectionTestUtils.setField(modifyTagRequestDto, "colorCode", "#47F1ZZ");

        // when
        doNothing().when(tagService).modifyTagInformation(modifyTagRequestDto);

        // then
        mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("지원하지 않는 색상 코드입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("태그 삭제 성공 테스트")
    void delete_tag_success_test() throws Exception {
        // given

        // when
        doNothing().when(tagService).deleteTagByTagNumber(anyInt());

        // then
        mockMvc.perform(delete("/api/tags/{tagNo}", anyInt()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(tagService, times(1))
                .deleteTagByTagNumber(anyInt());
    }

}