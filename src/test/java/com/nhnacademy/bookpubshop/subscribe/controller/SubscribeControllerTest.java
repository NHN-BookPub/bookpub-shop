package com.nhnacademy.bookpubshop.subscribe.controller;

import static com.nhnacademy.bookpubshop.subscribe.dummy.SubscribeDummy.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeProductRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.CreateSubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.request.ModifySubscribeRequestDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeDetailResponseDto;
import com.nhnacademy.bookpubshop.subscribe.dto.response.GetSubscribeProductListDto;
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

/**
 * ?????? ???????????? ??????????????????.
 *
 * @author : ?????????
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
    GetSubscribeDetailResponseDto getSubscribeDetailResponseDto;
    GetSubscribeProductListDto getSubscribeProductListDto;
    String tokenPath = "/token/subscribes";
    String apiPath = "/api/subscribes";
    String imageContent;
    MockMultipartFile multipartFile;
    MockMultipartFile dto;

    @BeforeEach
    void setUp() {
        getSubscribeProductListDto = listDto();
        objectMapper = new ObjectMapper();
        getSubscribeDetailResponseDto = detailDummy();
        imageContent = "234kh2kl4h2l34k2j34hlk23h4";
        multipartFile = new MockMultipartFile("image", "imageName.jpeg", "image/jpeg", imageContent.getBytes());
        modifySubscribeRequestDto = modifyDummy();
        getSubscribeResponseDto = responseDummy();
        createSubscribeRequestDto = new CreateSubscribeRequestDto();
    }

    @DisplayName("?????? ?????? validation ????????????")
    @Test
    void subscribeAddFailName() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdfsapdofsadkfpodsakfpsapfkakfpofkopsakdfpkasdpoksasdfdsafsadpo");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("????????? ????????? ?????? ?????????."))
                .andDo(print())
                .andDo(document("subscribe_add_fail_name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? ????????? ?????? ?????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? validation ??????????????????")
    @Test
    void subscribeAddFailNameEmpty() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("??????????????? ??????????????? ????????????."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-name-blank",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("??????????????? ??????????????? ????????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? validation Fail ????????????")
    @Test
    void subscribeAddFailSalePrice() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", -1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 1000L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("?????? ????????? 0 ????????? ???????????? ????????????."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-salePrice",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("?????? ????????? 0 ????????? ???????????? ????????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? validation Fail ??????")
    @Test
    void subscribeAddFailPrice() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", -111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 0);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("????????? 0 ????????? ???????????? ????????????."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-price",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("????????? 0 ????????? ???????????? ????????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? validation Fail ????????? ??????")
    @Test
    void subscribeAddFailSaleRateMin() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", -1);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);
        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("???????????? ?????? 0?????????."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-saleRate-min",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ?????? 0?????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? validation Fail ????????? ??????")
    @Test
    void subscribeAddFailSaleRateMax() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 111);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);
        doNothing().when(service).createSubscribe(createSubscribeRequestDto, null);

        dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(createSubscribeRequestDto).getBytes(StandardCharsets.UTF_8));
        mvc.perform(multipart(tokenPath)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("???????????? ?????? 100?????????."))
                .andDo(print())
                .andDo(document("subscribe-add-fail-saleRate-max",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].message").description("???????????? ?????? 100?????????.")
                        )
                ));
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void subscribeAddSuccess() throws Exception {
        ReflectionTestUtils.setField(createSubscribeRequestDto, "name", "asdf");
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salePrice", 1111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "price", 111L);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "salesRate", 10);
        ReflectionTestUtils.setField(createSubscribeRequestDto, "renewed", true);


        doNothing().when(service).createSubscribe(createSubscribeRequestDto, multipartFile);
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
                                partWithName("image").description("??????????????? ???????????? ?????????"),
                                partWithName("dto").description("??????????????? ????????? ??????")
                        )
                ));
        then(service).should().createSubscribe(any(), any());
    }

    @DisplayName("?????? ????????? ??????")
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
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].subscribeNo").description("???????????? ??????"),
                                fieldWithPath("content[].subscribeName").description("????????? ??????"),
                                fieldWithPath("content[].price").description("?????? ??? ?????? ??????"),
                                fieldWithPath("content[].salePrice").description("?????? ???????????? ??????"),
                                fieldWithPath("content[].salesRate").description("?????? ????????? ??????"),
                                fieldWithPath("content[].viewCnt").description("?????? ViewCnt ??????"),
                                fieldWithPath("content[].deleted").description("???????????? ??????"),
                                fieldWithPath("content[].renewed").description("???????????? ??????"),
                                fieldWithPath("content[].imagePath").description("????????? ??????"),
                                fieldWithPath("totalPages").description("??? ????????? ??? ?????????."),
                                fieldWithPath("number").description("?????? ????????? ?????????."),
                                fieldWithPath("previous").description("??????????????? ?????? ?????? ?????????."),
                                fieldWithPath("next").description("??????????????? ?????? ?????? ?????????.")
                        )
                ));
        then(service).should().getSubscribes(pageRequest);
    }

    @DisplayName("?????? ?????? ?????????")
    @Test
    void subscribeDelete() throws Exception {
        doNothing().when(service).deleteSubscribe(anyLong(), anyBoolean());

        mvc.perform(RestDocumentationRequestBuilders.delete(tokenPath + "/{subscribeNo}", 1L)
                        .param("isDeleted", objectMapper.writeValueAsString(true))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("subscribe-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("subscribeNo").description("??????????????? ??????"))));

        then(service).should().deleteSubscribe(1L, true);
    }

    @DisplayName("???????????? ?????? ?????? ?????????")
    @Test
    void subscribeModify() throws Exception {
        doNothing().when(service).modifySubscribe(any(ModifySubscribeRequestDto.class), anyLong(), any());

        MockMultipartFile dto = new MockMultipartFile("dto", "", "application/json",
                objectMapper.writeValueAsString(modifySubscribeRequestDto).getBytes(StandardCharsets.UTF_8));

        mvc.perform(multipart(tokenPath + "/{subscribeNo}", 1L)
                        .file(multipartFile)
                        .file(dto)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("subscribe-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("dto").description("?????? ?????????????????? ???????????????."),
                                partWithName("image").description("?????? ?????? ???????????? ???????????????.")
                        )
                ));
    }

    @DisplayName("????????? ??????????????? ?????? ??????")
    @Test
    void subscribeDetail() throws Exception {
        when(service.getSubscribeDetail(anyLong()))
                .thenReturn(getSubscribeDetailResponseDto);

        mvc.perform(get(apiPath + "/{subscribeNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscribeNo").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.getSubscribeNo())))
                .andExpect(jsonPath("$.subscribeName").value(getSubscribeDetailResponseDto.getSubscribeName()))
                .andExpect(jsonPath("$.price").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.getPrice())))
                .andExpect(jsonPath("$.salePrice").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.getSalePrice())))
                .andExpect(jsonPath("$.salesRate").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.getSalesRate())))
                .andExpect(jsonPath("$.viewCnt").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.getViewCnt())))
                .andExpect(jsonPath("$.deleted").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.isDeleted())))
                .andExpect(jsonPath("$.renewed").value(objectMapper.writeValueAsString(getSubscribeDetailResponseDto.isRenewed())))
                .andExpect(jsonPath("$.productLists[0].productNo").value(objectMapper.writeValueAsString(getSubscribeProductListDto.getProductNo())))
                .andExpect(jsonPath("$.productLists[0].title").value((getSubscribeProductListDto.getTitle())))
                .andExpect(jsonPath("$.productLists[0].filePath").value((getSubscribeProductListDto.getFilePath())))
                .andDo(print())
                .andDo(document("subscribe-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("subscribeNo").description("??????????????? ??????")),
                        responseFields(
                                fieldWithPath("subscribeNo").description("???????????? ??????"),
                                fieldWithPath("subscribeName").description("???????????? ??????"),
                                fieldWithPath("price").description("???????????? ??????"),
                                fieldWithPath("salePrice").description("??????????????? ??????"),
                                fieldWithPath("salesRate").description("???????????? ??????"),
                                fieldWithPath("viewCnt").description("??? ???????????? ??????"),
                                fieldWithPath("deleted").description("??????????????? ??????"),
                                fieldWithPath("renewed").description("??????????????? ??????"),
                                fieldWithPath("imagePath").description("????????? ????????? ??????"),
                                fieldWithPath("productLists[].productNo").description("??????????????? ??????"),
                                fieldWithPath("productLists[].title").description("???????????? ??????"),
                                fieldWithPath("productLists[].filePath").description("?????? ????????? ??????")
                        )
                ));
    }

    @DisplayName("?????? ???????????? ?????? ??????")
    @Test
    void renewedSuccess() throws Exception {
        doNothing().when(service)
                .modifySubscribeRenewed(anyLong(), anyBoolean());

        mvc.perform(put(tokenPath + "/{subscribeNo}", 1L)
                        .param("isRenewed", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("subscribe-renewed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("subscribeNo").description("??????????????? ??????")),
                        requestParameters(parameterWithName("isRenewed").description("??????????????? ??????"))
                ));

    }

    @DisplayName("????????? ???????????? ?????? ??????")
    @Test
    void relationProductListFail() throws Exception {
        CreateSubscribeProductRequestDto requestDto = new CreateSubscribeProductRequestDto();

        mvc.perform(post(tokenPath + "/{subscribeNo}/product-list", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("[0].message").value("?????? ???????????? ??????????????? ????????????."))
                .andDo(print())
                .andDo(document("subscribe-product-list-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("subscribeNo").description("??????????????? ??????"))
                ));

    }

    @DisplayName("????????? ???????????? ?????? ??????")
    @Test
    void relationProductListSuccess() throws Exception {
        CreateSubscribeProductRequestDto requestDto = new CreateSubscribeProductRequestDto();
        ReflectionTestUtils.setField(requestDto, "productNo", List.of(1L));
        mvc.perform(post(tokenPath + "/{subscribeNo}/product-list", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("subscribe-product-list-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("subscribeNo").description("??????????????? ??????")),
                        requestFields(
                                fieldWithPath("productNo.[]").description("?????????????????? ??????")
                        )
                ));

    }
}