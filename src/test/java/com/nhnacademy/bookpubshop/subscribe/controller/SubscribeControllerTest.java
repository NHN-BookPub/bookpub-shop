package com.nhnacademy.bookpubshop.subscribe.controller;

import static com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy.modifyDummy;
import static com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy.responseDummy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeResponseDto;
import com.nhnacademy.bookpubshop.subscribe.service.SubscribeService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

/**
 * 구독 컨트롤러 테스트입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@WebMvcTest(SubscribeController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class SubscribeControllerTest {

    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper;
    @MockBean
    SubscribeService service;
    CreateSubscribeRequestDto createSubscribeRequestDto;
    GetSubscribeResponseDto getSubscribeResponseDto;
    ModifySubscribeRequestDto modifySubscribeRequestDto;
    String tokenPath = "/token/subscribes";
    String apiPath = "/api/subscribes";
    String imageContent;
    MockMultipartFile multipartFile;
    MockMultipartFile dto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();

        imageContent="234kh2kl4h2l34k2j34hlk23h4";
        multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());
        modifySubscribeRequestDto = modifyDummy();
        getSubscribeResponseDto = responseDummy();
        createSubscribeRequestDto = new CreateSubscribeRequestDto();
    }

    @DisplayName("구독 생성 validation 이름길이")
    @Test
    void subscribeAddFailName() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdfsapdofsadkfpodsakfpsapfkakfpofkopsakdfpkasdpoksasdfdsafsadpo");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("이름의 길이가 너무 깁니다."))
                .andDo(print())
                .andDo(document("subscribe_add_fail_name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("이름의 길이가 너무 깁니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 validation 이름비어있음")
    @Test
    void subscribeAddFailNameEmpty() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("구독이름은 비어있을수 업습니다."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-name-blank",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("구독이름은 비어있을수 업습니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 validation Fail 할인가격")
    @Test
    void subscribeAddFailSalePrice() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", -1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("할인 가격이 0 이하로 내려갈순 없습니다."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-salePrice",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("할인 가격이 0 이하로 내려갈순 없습니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 validation Fail 가격")
    @Test
    void subscribeAddFailPrice() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", -111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("가격이 0 이하로 내려갈순 없습니다."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-price",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("가격이 0 이하로 내려갈순 없습니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 validation Fail 할인률 최소")
    @Test
    void subscribeAddFailSaleRateMin() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", -1);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("할인률은 최소 0입니다."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-saleRate-min",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("할인률은 최소 0입니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 validation Fail 할인률 최대")
    @Test
    void subscribeAddFailSaleRateMax() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 111);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto,null);

        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));
        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("할인률은 최대 100입니다."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-saleRate-max",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("할인률은 최대 100입니다.")
                        )
                ));
    }

    @DisplayName("구독 생성 성공")
    @Test
    void subscribeAddSuccess() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 10);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);


        doNothing().when(service).createSubscribe(createSubscribeRequestDto,multipartFile);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));
        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("subscribe-add-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("구독생성시 사용되는 이미지"),
                                partWithName("dto").description("구동생성시 필요한 정보")
                        )
                ));
        then(service).should().createSubscribe(any(),any());
    }

    @DisplayName("구독 리스트 조회")
    @Test
    void subscribeList() throws Exception {
        PageImpl<GetSubscribeResponseDto> response = new PageImpl<>(List.of(getSubscribeResponseDto));
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(service.getSubscribes(any(Pageable.class)))
                .thenReturn(response);

        mvc.perform(RestDocumentationRequestBuilders.get(apiPath)
                        .param("page", objectMapper.writeValueAsString(pageRequest.getPageNumber()))
                        .param("size", objectMapper.writeValueAsString(pageRequest.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].subscribeNo").value(getSubscribeResponseDto.getSubscribeNo()))
                .andExpect(jsonPath("$.content[0].subscribeName").value(getSubscribeResponseDto.getSubscribeName()))
                .andExpect(jsonPath("$.content[0].price").value(getSubscribeResponseDto.getPrice()))
                .andExpect(jsonPath("$.content[0].salePrice").value(getSubscribeResponseDto.getSalePrice()))
                .andExpect(jsonPath("$.content[0].salesRate").value(getSubscribeResponseDto.getSalesRate()))
                .andExpect(jsonPath("$.content[0].viewCnt").value(getSubscribeResponseDto.getViewCnt()))
                .andExpect(jsonPath("$.content[0].deleted").value(getSubscribeResponseDto.isDeleted()))
                .andExpect(jsonPath("$.content[0].renewed").value(getSubscribeResponseDto.isRenewed()))
                .andDo(print())
                .andDo(document("subscribe-getList",
                        preprocessRequest(),
                        preprocessResponse(),
                        responseFields(
                                fieldWithPath("content[].subscribeNo").description("구독번호 반환"),
                                fieldWithPath("content[].subscribeName").description("구독명 반환"),
                                fieldWithPath("content[].price").description("구독 원 가격 반환"),
                                fieldWithPath("content[].salePrice").description("구독 할인가격 반환"),
                                fieldWithPath("content[].salesRate").description("구독 할인률 반환"),
                                fieldWithPath("content[].viewCnt").description("구독 ViewCnt 반환"),
                                fieldWithPath("content[].deleted").description("삭제여부 반환"),
                                fieldWithPath("content[].renewed").description("갱신여부 반환"),
                                fieldWithPath("content[].imagePath").description("이미지 경로"),
                                fieldWithPath("totalPages").description("총 페이지 수 입니다."),
                                fieldWithPath("number").description("현재 페이지 입니다."),
                                fieldWithPath("previous").description("이전페이지 존재 여부 입니다."),
                                fieldWithPath("next").description("다음페이지 존재 여부 입니다.")
                        )
                ));
        then(service).should().getSubscribes(pageRequest);
    }

    @DisplayName("구독 삭제 테스트")
    @Test
    void subscribeDelete() throws Exception {
        doNothing().when(service).deleteSubscribe(anyLong(), anyBoolean());

        mvc.perform(RestDocumentationRequestBuilders.delete(tokenPath + "/{subscribeNo}", 1L)
                        .param("isDeleted", objectMapper.writeValueAsString(true))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("subscribe-delete",
                        preprocessRequest(),
                        preprocessResponse()));

        then(service).should().deleteSubscribe(1L, true);
    }

    @DisplayName("구독정보 수정 성공 테스트")
    @Test
    void subscribeModify() throws Exception {
        doNothing().when(service).modifySubscribe(any(ModifySubscribeRequestDto.class), anyLong(),any());

        MockMultipartFile dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(modifySubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath + "/{subscribeNo}", 1L)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("subscribe-modify",
                        preprocessRequest(),
                        preprocessResponse(),
                        requestParts(
                                partWithName("dto").description("구독 수정정보들이 들어옵니다."),
                                partWithName("image").description("구독 수정 이미지가 들어옵니다.")
                        )
                ));
    }
}