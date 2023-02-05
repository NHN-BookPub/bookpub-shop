package com.nhnacademy.bookpubshop.tag.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TagService tagService;

    ObjectMapper mapper;
    AddTagRequestDto addTagRequestDto;
    ModifyTagRequestDto modifyTagRequestDto;

    String path = "/api/tags";
    String tokenPath = "/token/tags";

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
        mockMvc.perform(RestDocumentationRequestBuilders.get(path + "/{tagNo}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagNo").value(getTagResponseDto.getTagNo()))
                .andExpect(jsonPath("$.tagName").value(getTagResponseDto.getTagName()))
                .andExpect(jsonPath("$.colorCode").value(getTagResponseDto.getColorCode()))
                .andDo(print())
                .andDo(document("tag-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("tagNo").description("태그 번호")),
                        responseFields(
                                fieldWithPath("tagNo").description("태그 번호"),
                                fieldWithPath("tagName").description("태그 이름"),
                                fieldWithPath("colorCode").description("태그 색상 코드")
                        )));

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
        mockMvc.perform(get(path)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagNo").value(getTagResponseDto.getTagNo()))
                .andExpect(jsonPath("$[0].tagName").value(getTagResponseDto.getTagName()))
                .andExpect(jsonPath("$[0].colorCode").value(getTagResponseDto.getColorCode()))
                .andDo(print())
                .andDo(document("tag-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].tagNo").description("태그 번호"),
                                fieldWithPath("[].tagName").description("태그 이름"),
                                fieldWithPath("[].colorCode").description("태그 색상 코드")
                        )));

        verify(tagService, times(1))
                .getTags();
    }

    @Test
    @DisplayName("태그 생성 성공 테스트")
    void add_tagSuccess_test() throws Exception {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "addTagName", "강추");
        ReflectionTestUtils.setField(addTagRequestDto, "addColorCode", "#E1FFE1");

        // when
        doNothing().when(tagService).addTag(addTagRequestDto);

        // then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addTagRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tag-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addTagName").description("태그 이름 기입"),
                                fieldWithPath("addColorCode").description("태그 색상 코드 기입")
                        )));

        then(tagService).should().addTag(any(AddTagRequestDto.class));
    }

    @Test
    @DisplayName("태그 생성 실패 테스트 (Validation Exception) - tagNameValidation")
    void add_tag_fail_tagNameValidation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "addTagName", "");
        ReflectionTestUtils.setField(addTagRequestDto, "addColorCode", "#E1FFE1");

        ArgumentCaptor<AddTagRequestDto> captor = ArgumentCaptor.forClass(
                AddTagRequestDto.class);

        // when
        doNothing().when(tagService).addTag(addTagRequestDto);

        // then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("태그 이름은 최소 1글자, 최대 20글자 가능합니다."))
                .andDo(print())
                .andDo(document("tag-create-tagNameFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addTagName").description("태그 이름 기입"),
                                fieldWithPath("addColorCode").description("태그 색상 코드 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description(
                                        "태그 이름은 최소 1글자, 최대 20글자 가능합니다.")
                        )));
        verify(tagService, times(0))
                .addTag(captor.capture());

    }


    @Test
    @DisplayName("태그 생성 실패 테스트 (Validation Exception) - ColorCodeValidation")
    void add_tag_fail_ColorCodeValidation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(addTagRequestDto, "addTagName", "태그");
        ReflectionTestUtils.setField(addTagRequestDto, "addColorCode", "#PIUYT1");

        ArgumentCaptor<AddTagRequestDto> captor = ArgumentCaptor.forClass(
                AddTagRequestDto.class);

        // when
        doNothing().when(tagService).addTag(addTagRequestDto);

        // then
        mockMvc.perform(post(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(addTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("지원하지 않는 색상 코드입니다."))
                .andDo(print())
                .andDo(document("tag-create-colorCodeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addTagName").description("태그 이름 기입"),
                                fieldWithPath("addColorCode").description("태그 색상코드 기입")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("지원하지 않는 색상 코드입니다.")
                        )));

        verify(tagService, times(0)).addTag(captor.capture());
    }

    @Test
    @DisplayName("태그 수정 성공 테스트")
    void modify_tag_success_test() throws Exception {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName", "변경태그");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#47FF9C");

        // when
        doNothing().when(tagService).modifyTagInformation(modifyTagRequestDto);

        // then
        mockMvc.perform(put(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyTagRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tag-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("modifyTagNo").description("수정할 태그 번호"),
                                fieldWithPath("modifyTagName").description("수정할 태그 이름"),
                                fieldWithPath("modifyColorCode").description("수정할 태그 색상 코드")
                        )));

        then(tagService).should().modifyTagInformation(any(ModifyTagRequestDto.class));
    }

    @Test
    @DisplayName("태그 수정 실패 테스트 (Validation Exception) - tagNameValidation")
    void modify_tag_fail_tagName_Validation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName",
                "태그A1234567891011121314");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#47FF9C");

        ArgumentCaptor<ModifyTagRequestDto> captor = ArgumentCaptor.forClass(
                ModifyTagRequestDto.class);

        // when
        doNothing().when(tagService).modifyTagInformation(modifyTagRequestDto);

        // then
        mockMvc.perform(put(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("태그 이름은 최소 1글자, 최대 20글자 가능합니다."))
                .andDo(print())
                .andDo(document("tag-modify-tagNameFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("modifyTagNo").description("수정할 태그 번호"),
                                fieldWithPath("modifyTagName").description("수정할 태그 이름"),
                                fieldWithPath("modifyColorCode").description("수정할 태그 색상 코드")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description(
                                        "태그 이름은 최소 1글자, 최대 20글자 가능합니다.")
                        )));

        verify(tagService, times(0)).modifyTagInformation(captor.capture());
    }

    @Test
    @DisplayName("태그 수정 실패 테스트 (Validation Exception) - colorCodeValidation")
    void modify_tag_fail_colorCode_validation_exception_test() throws Exception {
        // given
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagNo", 1);
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyTagName", "태그A");
        ReflectionTestUtils.setField(modifyTagRequestDto, "modifyColorCode", "#47F1ZZdaf");

        ArgumentCaptor<ModifyTagRequestDto> captor = ArgumentCaptor.forClass(
                ModifyTagRequestDto.class);

        // when
        doNothing().when(tagService).modifyTagInformation(modifyTagRequestDto);

        // then
        mockMvc.perform(put(tokenPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(modifyTagRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("지원하지 않는 색상 코드입니다."))
                .andDo(print())
                .andDo(document("tag-modify-colorCodeFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("modifyTagNo").description("수정할 태그 번호"),
                                fieldWithPath("modifyTagName").description("수정할 태그 이름"),
                                fieldWithPath("modifyColorCode").description("수정할 태그 색상 코드")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("지원하지 않는 색상 코드입니다.")
                        )));

        verify(tagService, times(0)).modifyTagInformation(captor.capture());
    }

    @Test
    @DisplayName("태그 삭제 성공 테스트")
    void delete_tag_success_test() throws Exception {
        // given

        // when
        doNothing().when(tagService).deleteTagByTagNumber(anyInt());

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(tokenPath + "/{tagNo}", anyInt()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("tag-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tagNo").description("태그 번호")
                        )
                ));

        verify(tagService, times(1))
                .deleteTagByTagNumber(anyInt());
    }

}