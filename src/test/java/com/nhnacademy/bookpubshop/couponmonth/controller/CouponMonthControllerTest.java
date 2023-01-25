package com.nhnacademy.bookpubshop.couponmonth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.CreateCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.request.ModifyCouponMonthRequestDto;
import com.nhnacademy.bookpubshop.couponmonth.dto.response.GetCouponMonthResponseDto;
import com.nhnacademy.bookpubshop.couponmonth.service.CouponMonthService;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 이달의 쿠폰 컨트롤러 테스트.
 *
 * @author : 정유진
 * @since : 1.0
 **/
@WebMvcTest(CouponMonthController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponMonthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CouponMonthService couponMonthService;

    ObjectMapper mapper;
    String url = "/api/coupon-months";

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("쿠폰 등록 api 테스트")
    void couponMonthAdd_Success_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(couponMonthService).should()
                .createCouponMonth(any(CreateCouponMonthRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (templateNo validation)")
    void couponMonthAdd_TemplateNo_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", null);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 템플릿 번호를 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (openedAt validation)")
    void couponMonthAdd_OpenedAt_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", null);
        ReflectionTestUtils.setField(request, "monthQuantity", 100);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 오픈 시간을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (monthQuantity null)")
    void couponMonthAdd_NullMonthQuantity_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", null);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("수량을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 등록 api 실패 테스트 (monthQuantity null)")
    void couponMonthAdd_NegativeMonthQuantity_Validation_Fail_Test() throws Exception {
        // given
        CreateCouponMonthRequestDto request = new CreateCouponMonthRequestDto();
        ReflectionTestUtils.setField(request, "templateNo", 1L);
        ReflectionTestUtils.setField(request, "openedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(request, "monthQuantity", -1);

        // when
        doNothing().when(couponMonthService).createCouponMonth(request);

        // then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0이상의 수량을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("이달의 쿠폰 수정 성공 테스트")
    void couponMonthModify_Success_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        then(couponMonthService).should()
                .modifyCouponMonth(any(ModifyCouponMonthRequestDto.class));
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (쿠폰 번호가 없는 경우)")
    void couponMonthModify_monthNo_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(null, LocalDateTime.now(), 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(jsonPath("$[0].message").value("수정할 이달의 쿠폰 번호를 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (오픈시간 null)")
    void couponMonthModify_openedAt_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, null, 100);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("쿠폰 오픈 시간을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (monthQuantity null)")
    void couponMonthModify_NUllQuantity_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), null);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("수량을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("쿠폰 수정 api 실패 테스트 (monthQuantity 음수")
    void couponMonthModify_NegativeQuantity_Validation_Test() throws Exception {
        // given
        ModifyCouponMonthRequestDto request = new ModifyCouponMonthRequestDto(1L, LocalDateTime.now(), -1);

        // when
        doNothing().when(couponMonthService).modifyCouponMonth(request);

        // then
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$[0].message").value("0이상의 수량을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("이달의 쿠폰 삭제 성공 테스트")
    void couponMonthDelete_Success_Test() throws Exception {
        // given

        // when
        doNothing().when(couponMonthService).deleteCouponMonth(anyLong());

        // then
        mockMvc.perform(delete(url + "/{monthNo}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    @DisplayName("이달의 쿠폰 전체 테스트")
    void couponMonthList_Success_Test() throws Exception {
        // given
        GetCouponMonthResponseDto response = new GetCouponMonthResponseDto(1L, 1L, "name", "image", LocalDateTime.now(), 100);
        List<GetCouponMonthResponseDto> list = List.of(response);

        // when
        when(couponMonthService.getCouponMonths())
                .thenReturn(list);

        // then
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monthNo").value(response.getMonthNo()))
                .andExpect(jsonPath("$[0].templateNo").value(response.getTemplateNo()))
                .andExpect(jsonPath("$[0].templateName").value(response.getTemplateName()))
                .andExpect(jsonPath("$[0].templateImage").value(response.getTemplateImage()))
                .andExpect(jsonPath("$[0].monthQuantity").value(response.getMonthQuantity()))
                .andDo(print());

        then(couponMonthService).should()
                .getCouponMonths();
    }

}