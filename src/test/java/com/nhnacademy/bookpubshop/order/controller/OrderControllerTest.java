package com.nhnacademy.bookpubshop.order.controller;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.service.OrderService;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 주문 컨트롤러 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@WebMvcTest(OrderController.class)
@Import(ShopAdviceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    ObjectMapper mapper;
    BookpubOrder order;
    BookPubTier tier;
    Member member;
    PricePolicy pricePolicy;
    PricePolicy packPricePolicy;
    OrderStateCode orderStateCode;
    Product product;
    CreateOrderRequestDto requestDto;
    GetOrderDetailResponseDto detailDto;
    GetOrderListResponseDto listDto;
    GetProductListForOrderResponseDto productDto;
    GetOrderListForAdminResponseDto adminListDto;
    List<GetProductListForOrderResponseDto> products = new ArrayList<>();
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    OrderProduct orderProduct;
    OrderProductStateCode orderProductStateCode;
    List<GetOrderListResponseDto> orders = new ArrayList<>();
    List<GetOrderListForAdminResponseDto> adminOrders = new ArrayList<>();
    Pageable pageable;
    Page<GetOrderListResponseDto> pages;
    Page<GetOrderListForAdminResponseDto> adminPages;
    String url = "/api/orders";
    String tokenUrl="/token/orders";


    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        tier = TierDummy.dummy();
        member = MemberDummy.dummy(tier);
        pricePolicy = PricePolicyDummy.dummy(1);
        packPricePolicy = PricePolicyDummy.dummy(2);
        orderStateCode = OrderStateCodeDummy.dummy(1);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();

        order = OrderDummy.dummy(member, pricePolicy, packPricePolicy, orderStateCode);
        detailDto = new GetOrderDetailResponseDto(
                1L,
                order.getOrderStateCode().getCodeName(),
                order.getOrderBuyer(),
                order.getBuyerPhone(),
                order.getOrderRecipient(),
                order.getRecipientPhone(),
                order.getRoadAddress(),
                order.getAddressDetail(),
                order.getCreatedAt(),
                order.getReceivedAt(),
                order.getInvoiceNumber(),
                order.isOrderPackaged(),
                order.getPackagingPricePolicy().getPolicyFee(),
                order.getDeliveryPricePolicy().getPolicyFee(),
                order.getOrderRequest(),
                order.getPointAmount(),
                order.getCouponDiscount(),
                order.getOrderPrice());

        orderProductStateCode = new OrderProductStateCode(
                null,
                OrderProductState.COMPLETE_PAYMENT.getName(),
                true,
                "info");

        orderProduct = new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        product.setProductFiles(List.of(
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_THUMBNAIL),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_DETAIL),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_EBOOK)));

        new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");

        productDto = new GetProductListForOrderResponseDto(1L,
                product.getTitle(), product.getFiles().get(0).getFilePath(), product.getSalesPrice(), orderProduct.getProductAmount(), orderProductStateCode.getCodeName());

        products.add(productDto);

        requestDto = new CreateOrderRequestDto();
        Map<Long, Long> amounts = new HashMap<>();
        amounts.put(1L, 3L);
        Map<Long, Long> couponAmount = new HashMap<>();
        couponAmount.put(1L, 2000L);
        Map<Long, Integer> productCount = new HashMap<>();
        productCount.put(1L, 1);
        Map<Long, Long> productSaleAmount = new HashMap<>();
        productSaleAmount.put(1L, 2000L);


        ReflectionTestUtils.setField(requestDto, "productNos", List.of(1L));
        ReflectionTestUtils.setField(requestDto, "productAmount", amounts);
        ReflectionTestUtils.setField(requestDto, "productCoupon", couponAmount);
        ReflectionTestUtils.setField(requestDto, "buyerName", order.getOrderBuyer());
        ReflectionTestUtils.setField(requestDto, "buyerNumber", order.getBuyerPhone());
        ReflectionTestUtils.setField(requestDto, "recipientName", order.getOrderRecipient());
        ReflectionTestUtils.setField(requestDto, "recipientNumber", order.getRecipientPhone());
        ReflectionTestUtils.setField(requestDto, "addressDetail", order.getAddressDetail());
        ReflectionTestUtils.setField(requestDto, "roadAddress", order.getRoadAddress());
        ReflectionTestUtils.setField(requestDto, "receivedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(requestDto, "packaged", order.isOrderPackaged());
        ReflectionTestUtils.setField(requestDto, "orderRequest", order.getOrderRequest());
        ReflectionTestUtils.setField(requestDto, "pointAmount", order.getPointAmount());
        ReflectionTestUtils.setField(requestDto, "couponAmount", order.getCouponDiscount());
        ReflectionTestUtils.setField(requestDto, "productCount",productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo",1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);
        ReflectionTestUtils.setField(requestDto,"orderName","주문명");


        listDto = new GetOrderListResponseDto(
                order.getOrderNo(),
                order.getOrderStateCode().getCodeName(),
                order.getCreatedAt(),
                order.getReceivedAt(),
                order.getInvoiceNumber(),
                order.getOrderPrice()
        );

        listDto.addOrderProducts(List.of(productDto));
        orders.add(listDto);

        pageable = PageRequest.of(0, 10);

        pages = PageableExecutionUtils.getPage(orders, pageable, orders::size);

        adminListDto = new GetOrderListForAdminResponseDto(
                order.getOrderNo(),
                order.getMember().getMemberId(),
                order.getCreatedAt(),
                order.getInvoiceNumber(),
                order.getOrderStateCode().getCodeName(),
                order.getOrderPrice(),
                order.getReceivedAt()
        );

        adminOrders.add(adminListDto);

        adminPages = PageableExecutionUtils.getPage(adminOrders, pageable, adminOrders::size);
    }

    @Test
    @DisplayName("전체 주문 조회 성공")
    void getOrders() throws Exception {
        when(orderService.getOrderList(pageable))
                .thenReturn(new PageResponse<>(adminPages));

        mockMvc.perform(get(tokenUrl)
                        .param("page", mapper.writeValueAsString(pageable.getPageNumber()))
                        .param("size", mapper.writeValueAsString(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new PageResponse<>(adminPages))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].orderNo")
                        .value(adminListDto.getOrderNo()))
                .andExpect(jsonPath("$.content[0].memberId")
                        .value(adminListDto.getMemberId()))
                .andExpect(jsonPath("$.content[0].orderState")
                        .value(adminListDto.getOrderState()))
                .andExpect(jsonPath("$.content[0].invoiceNo")
                        .value(adminListDto.getInvoiceNo()))
                .andExpect(jsonPath("$.content[0].totalAmount").
                        value(adminListDto.getTotalAmount()))
                .andDo(print())
                .andDo(document("order-list",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("content[].orderNo").description("주문번호"),
                                fieldWithPath("content[].memberId").description("회원 아이디"),
                                fieldWithPath("content[].createdAt").description("주문 일"),
                                fieldWithPath("content[].receivedAt").description("받는 일자"),
                                fieldWithPath("content[].invoiceNo").description("운송장 번호"),
                                fieldWithPath("content[].totalAmount").description("주문 수량"),
                                fieldWithPath("content[].orderState").description("주문상태"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));

        verify(orderService, times(1)).getOrderList(pageable);

    }

    @Test
    @DisplayName("주문 등록 성공")
    void createOrder() throws Exception {
        when(orderService.createOrder(requestDto)).thenReturn(1L);

        mockMvc.perform(post(url)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("order-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        PayloadDocumentation.subsectionWithPath("productAmount")
                                                .description("상품 수량"),
                                        PayloadDocumentation.subsectionWithPath("productCoupon")
                                                .description("상품에 사용된 쿠폰"),
                                        PayloadDocumentation.subsectionWithPath("productCount")
                                                .description("상품별 구입 갯수"),
                                        PayloadDocumentation.subsectionWithPath("productSaleAmount")
                                                .description("상품에 대한 할인 금액"),
                                        fieldWithPath("productNos").description("상품번호"),
                                        fieldWithPath("productAmount").description("상품수량"),
                                        fieldWithPath("buyerName").description("주문인"),
                                        fieldWithPath("buyerNumber").description("주문인 번호"),
                                        fieldWithPath("recipientName").description("수령인"),
                                        fieldWithPath("recipientNumber").description("수령인 번호"),
                                        fieldWithPath("addressDetail").description("상세 주소"),
                                        fieldWithPath("roadAddress").description("도로명 주소"),
                                        fieldWithPath("receivedAt").description("수령 날짜"),
                                        fieldWithPath("packaged").description("포장 여부"),
                                        fieldWithPath("orderRequest").description("요청사항"),
                                        fieldWithPath("pointAmount").description("포안트 사용량"),
                                        fieldWithPath("couponAmount").description("쿠폰 할인 금액"),
                                        fieldWithPath("totalAmount").description("총 금액"),
                                        fieldWithPath("productCount").description("상품별 구매 개수 해시맵"),
                                        fieldWithPath("productSaleAmount").description("상품별 할인 금액"),
                                        fieldWithPath("memberNo").description("구입 회원"),
                                        fieldWithPath("deliveryFeePolicyNo").description("배송정책 번호"),
                                        fieldWithPath("packingFeePolicyNo").description("포장정책 번호"),
                                        fieldWithPath("savePoint").description("포인트 적립액수"),
                                        fieldWithPath("orderName").description("주문명")
                                )
                        )

                );

        verify(orderService, times(1))
                .createOrder(any());

    }

    @Test
    @DisplayName("송장번호 수정 성공")
    void modifyInvoiceNo() throws Exception {
        doNothing().when(orderService).modifyInvoiceNumber(order.getOrderNo(), "1231231231");

        mockMvc.perform(
                        RestDocumentationRequestBuilders.put(url + "/{orderNo}/invoice", 1)
                                .param("no", mapper.writeValueAsString(order.getInvoiceNumber()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("invoiceNo-modify",
                        pathParameters(
                                parameterWithName("orderNo").description("주문 번호")
                        ),
                        requestParameters(
                                parameterWithName("no").description("송장 번호")
                        )
                ));

        verify(orderService, times(1))
                .modifyInvoiceNumber(any(), anyString());
    }

    @Test
    @DisplayName("상태코드 수정 성공")
    void modifyStateCode() throws Exception {
        doNothing().when(orderService).modifyStateCode("결제완료", order.getOrderNo());

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{orderNo}/state", 1)
                        .param("code", mapper.writeValueAsString(order.getOrderStateCode()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("state-code-modify",
                        pathParameters(
                                parameterWithName("orderNo").description("주문 번호")),
                        requestParameters(
                                parameterWithName("code").description("주문 상태 코드")
                        )));

        verify(orderService, times(1))
                .modifyStateCode(anyString(), anyLong());
    }

    @Test
    @DisplayName("멤버 번호로 주문 조회 성공")
    void getOrdersByMember() throws Exception {
        when(orderService.getOrderListByUsers(any(), anyLong()))
                .thenReturn(new PageResponse<>(pages));

        mockMvc.perform(get(tokenUrl + "/member?page=0&size=10&no=" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-by-member-get",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].orderNo").description("주문번호"),
                                fieldWithPath("content[].orderProducts[].productNo").description(
                                        "주문상품의 상품 주문 번호"),
                                fieldWithPath("content[].orderProducts[].title").description(
                                        "주문상품의 상품명"),
                                fieldWithPath("content[].orderProducts[].salesPrice").description(
                                        "주문상품의 상품 할인가격"),
                                fieldWithPath(
                                        "content[].orderProducts[].productAmount").description(
                                        "주문상품의 상품 총량"),
                                fieldWithPath(
                                        "content[].orderProducts[].stateCode").description(
                                        "주문상품의 상태 코드"),
                                fieldWithPath(
                                        "content[].orderProducts[].thumbnail").description(
                                                "주문상품의 썸네일이미지"),
                                fieldWithPath("content[].orderState").description("주문 상태"),
                                fieldWithPath("content[].createdAt").description("주문 일"),
                                fieldWithPath("content[].receivedAt").description("받는 일자"),
                                fieldWithPath("content[].invoiceNo").description("운송장 번호"),
                                fieldWithPath("content[].totalAmount").description("주문 수량"),
                                fieldWithPath("content[].orderState").description("주문상태"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("number").description("현재 페이지 번호"),
                                fieldWithPath("previous").description("이전 페이지 번호"),
                                fieldWithPath("next").description("다음 페이지 번호")
                        )));

        verify(orderService, times(1)).getOrderListByUsers(pageable, 1L);
    }

    @Test
    @DisplayName("주문 상세 조회 성공")
    void getOrderDetailByOrderNo() throws Exception {
        when(orderService.getOrderDetailById(anyLong()))
                .thenReturn(detailDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(url + "/{orderNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderState").value(order.getOrderStateCode().getCodeName()))
                .andExpect(jsonPath("$.buyerName").value(order.getOrderBuyer()))
                .andExpect(jsonPath("$.buyerNumber").value(order.getBuyerPhone()))
                .andExpect(jsonPath("$.recipientName").value(order.getOrderRecipient()))
                .andExpect(jsonPath("$.recipientNumber").value(order.getRecipientPhone()))
                .andExpect(jsonPath("$.addressBase").value(order.getRoadAddress()))
                .andExpect(jsonPath("$.addressDetail").value(order.getAddressDetail()))
                .andExpect(jsonPath("$.createdAt").value(order.getCreatedAt()))
                .andExpect(jsonPath("$.invoiceNo").value(order.getInvoiceNumber()))
                .andExpect(jsonPath("$.packaged").value(order.isOrderPackaged()))
                .andDo(print())
                .andDo(document("order-detail",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderNo").description("주문번호")
                        ),
                        responseFields(
                                fieldWithPath("orderNo").description("주문번호"),
                                fieldWithPath("orderProducts").description("주문상품"),
                                fieldWithPath("orderState").description("결제완료"),
                                fieldWithPath("buyerName").description("구매자"),
                                fieldWithPath("buyerNumber").description("구매자 번호"),
                                fieldWithPath("recipientName").description("수령인"),
                                fieldWithPath("recipientNumber").description("수령인 번호"),
                                fieldWithPath("addressBase").description("기본 주소"),
                                fieldWithPath("addressDetail").description("상세 주소"),
                                fieldWithPath("createdAt").description("주문일"),
                                fieldWithPath("receivedAt").description("수령일"),
                                fieldWithPath("invoiceNo").description("송장 번호"),
                                fieldWithPath("packaged").description("포장여부"),
                                fieldWithPath("packageAmount").description("포장비"),
                                fieldWithPath("deliveryAmount").description("배송비"),
                                fieldWithPath("orderRequest").description("요구사항"),
                                fieldWithPath("pointAmount").description("포인트 사용량"),
                                fieldWithPath("couponAmount").description("쿠폰 할인 금액"),
                                fieldWithPath("totalAmount").description("총 금액")

                        )));
    }
}