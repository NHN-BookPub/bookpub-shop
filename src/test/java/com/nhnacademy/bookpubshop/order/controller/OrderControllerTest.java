package com.nhnacademy.bookpubshop.order.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderAndPaymentResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderConfirmResponseDto;
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
 * ?????? ???????????? ?????????.
 *
 * @author : ?????????, ?????????
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
    GetOrderAndPaymentResponseDto dto;
    String url = "/api/orders";
    String tokenUrl = "/token/orders";


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
                order.getMember().getMemberNo(),
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
                order.getOrderPrice(),
                order.getOrderName(),
                order.getOrderId());


        orderProductStateCode = new OrderProductStateCode(
                null,
                OrderProductState.COMPLETE_PAYMENT.getName(),
                true,
                "info");

        orderProduct = new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason", 100L, "");

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        product.setProductFiles(List.of(
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_THUMBNAIL, null),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_DETAIL, null),
                FileDummy.dummy(
                        null,
                        null,
                        null,
                        product,
                        null,
                        FileCategory.PRODUCT_EBOOK, null)));

        new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason", 100L, "");

        productDto = new GetProductListForOrderResponseDto(1L,
                orderProduct.getOrderProductNo(),
                product.getTitle(),
                product.getFiles().get(0).getFilePath(),
                product.getSalesPrice(),
                orderProduct.getProductAmount(),
                orderProductStateCode.getCodeName());

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
        Map<Long, Long> productPointSave = new HashMap<>();
        productSaleAmount.put(1L, 2000L);


        ReflectionTestUtils.setField(requestDto, "productNos", List.of(1L));
        ReflectionTestUtils.setField(requestDto, "productAmount", amounts);
        ReflectionTestUtils.setField(requestDto, "productCoupon", couponAmount);
        ReflectionTestUtils.setField(requestDto, "productPointSave", productPointSave);
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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);
        ReflectionTestUtils.setField(requestDto, "orderName", "?????????");


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

        dto = new GetOrderAndPaymentResponseDto(
                "ordername",
                "address",
                "recipient",
                null,
                15000L,
                1500L,
                "??????",
                "url"
        );
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
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
                                parameterWithName("page").description("????????? ????????? ??????"),
                                parameterWithName("size").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("content[].orderNo").description("????????????"),
                                fieldWithPath("content[].memberId").description("?????? ?????????"),
                                fieldWithPath("content[].createdAt").description("?????? ???"),
                                fieldWithPath("content[].receivedAt").description("?????? ??????"),
                                fieldWithPath("content[].invoiceNo").description("????????? ??????"),
                                fieldWithPath("content[].totalAmount").description("?????? ??????"),
                                fieldWithPath("content[].orderState").description("????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ??????")
                        )));

        verify(orderService, times(1)).getOrderList(pageable);

    }

    @Test
    @DisplayName("?????? ?????? ??????")
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
                                                .description("?????? ??????"),
                                        PayloadDocumentation.subsectionWithPath("productCoupon")
                                                .description("????????? ????????? ??????"),
                                        PayloadDocumentation.subsectionWithPath("productCount")
                                                .description("????????? ?????? ??????"),
                                        PayloadDocumentation.subsectionWithPath("productSaleAmount")
                                                .description("????????? ?????? ?????? ??????"),
                                        PayloadDocumentation.subsectionWithPath("productPointSave")
                                                .description("?????? ??? ???????????? ?????????"),
                                        fieldWithPath("productNos").description("????????????"),
                                        fieldWithPath("productAmount").description("????????????"),
                                        fieldWithPath("buyerName").description("?????????"),
                                        fieldWithPath("buyerNumber").description("????????? ??????"),
                                        fieldWithPath("recipientName").description("?????????"),
                                        fieldWithPath("recipientNumber").description("????????? ??????"),
                                        fieldWithPath("addressDetail").description("?????? ??????"),
                                        fieldWithPath("roadAddress").description("????????? ??????"),
                                        fieldWithPath("receivedAt").description("?????? ??????"),
                                        fieldWithPath("packaged").description("?????? ??????"),
                                        fieldWithPath("orderRequest").description("????????????"),
                                        fieldWithPath("pointAmount").description("????????? ?????????"),
                                        fieldWithPath("couponAmount").description("?????? ?????? ??????"),
                                        fieldWithPath("totalAmount").description("??? ??????"),
                                        fieldWithPath("productCount").description("????????? ?????? ?????? ?????????"),
                                        fieldWithPath("productSaleAmount").description("????????? ?????? ??????"),
                                        fieldWithPath("memberNo").description("?????? ??????"),
                                        fieldWithPath("deliveryFeePolicyNo").description("???????????? ??????"),
                                        fieldWithPath("packingFeePolicyNo").description("???????????? ??????"),
                                        fieldWithPath("savePoint").description("????????? ????????????"),
                                        fieldWithPath("orderName").description("?????????")
                                )
                        )

                );

        verify(orderService, times(1))
                .createOrder(any());

    }

    @Test
    @DisplayName("???????????? ?????? ??????")
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
                                parameterWithName("orderNo").description("?????? ??????")
                        ),
                        requestParameters(
                                parameterWithName("no").description("?????? ??????")
                        )
                ));

        verify(orderService, times(1))
                .modifyInvoiceNumber(any(), anyString());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void modifyStateCode() throws Exception {
        doNothing().when(orderService).modifyStateCode("????????????", order.getOrderNo());

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{orderNo}/state", 1)
                        .param("code", mapper.writeValueAsString(order.getOrderStateCode()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("state-code-modify",
                        pathParameters(
                                parameterWithName("orderNo").description("?????? ??????")),
                        requestParameters(
                                parameterWithName("code").description("?????? ?????? ??????")
                        )));

        verify(orderService, times(1))
                .modifyStateCode(anyString(), anyLong());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??????")
    void getOrdersByMember() throws Exception {
        when(orderService.getOrderListByUsers(any(), anyLong()))
                .thenReturn(new PageResponse<>(pages));

        mockMvc.perform(RestDocumentationRequestBuilders
                        .get(tokenUrl + "/member/{memberNo}", 1)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-by-member-get",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberNo").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("content[].orderNo").description("????????????"),
                                fieldWithPath("content[].orderProducts[].orderProductNo").description(
                                        "??????????????? ???????????? ??????"),
                                fieldWithPath("content[].orderProducts[].productNo").description(
                                        "??????????????? ?????? ?????? ??????"),
                                fieldWithPath("content[].orderProducts[].title").description(
                                        "??????????????? ?????????"),
                                fieldWithPath("content[].orderProducts[].salesPrice").description(
                                        "??????????????? ?????? ????????????"),
                                fieldWithPath(
                                        "content[].orderProducts[].productAmount").description(
                                        "??????????????? ?????? ??????"),
                                fieldWithPath(
                                        "content[].orderProducts[].stateCode").description(
                                        "??????????????? ?????? ??????"),
                                fieldWithPath(
                                        "content[].orderProducts[].thumbnail").description(
                                        "??????????????? ??????????????????"),
                                fieldWithPath("content[].orderState").description("?????? ??????"),
                                fieldWithPath("content[].createdAt").description("?????? ???"),
                                fieldWithPath("content[].receivedAt").description("?????? ??????"),
                                fieldWithPath("content[].invoiceNo").description("????????? ??????"),
                                fieldWithPath("content[].totalAmount").description("?????? ??????"),
                                fieldWithPath("content[].orderState").description("????????????"),
                                fieldWithPath("totalPages").description("??? ????????? ???"),
                                fieldWithPath("number").description("?????? ????????? ??????"),
                                fieldWithPath("previous").description("?????? ????????? ??????"),
                                fieldWithPath("next").description("?????? ????????? ??????")
                        )));

        verify(orderService, times(1)).getOrderListByUsers(pageable, 1L);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void getOrderDetailByOrderNo() throws Exception {
        when(orderService.getOrderDetailById(anyLong()))
                .thenReturn(detailDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(tokenUrl + "/{orderNo}/members/{memberNo}", 1L, 1L)
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
                .andExpect(jsonPath("$.couponAmount").value(order.getCouponDiscount()))
                .andExpect(jsonPath("$.totalAmount").value(order.getOrderPrice()))
                .andExpect(jsonPath("$.orderName").value(order.getOrderName()))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
                .andDo(print())
                .andDo(document("order-detail",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderNo").description("????????????"),
                                parameterWithName("memberNo").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("orderNo").description("????????????"),
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("orderProducts").description("????????????"),
                                fieldWithPath("orderState").description("????????????"),
                                fieldWithPath("buyerName").description("?????????"),
                                fieldWithPath("buyerNumber").description("????????? ??????"),
                                fieldWithPath("recipientName").description("?????????"),
                                fieldWithPath("recipientNumber").description("????????? ??????"),
                                fieldWithPath("addressBase").description("?????? ??????"),
                                fieldWithPath("addressDetail").description("?????? ??????"),
                                fieldWithPath("createdAt").description("?????????"),
                                fieldWithPath("receivedAt").description("?????????"),
                                fieldWithPath("invoiceNo").description("?????? ??????"),
                                fieldWithPath("packaged").description("????????????"),
                                fieldWithPath("packageAmount").description("?????????"),
                                fieldWithPath("deliveryAmount").description("?????????"),
                                fieldWithPath("orderRequest").description("????????????"),
                                fieldWithPath("pointAmount").description("????????? ?????????"),
                                fieldWithPath("couponAmount").description("?????? ?????? ??????"),
                                fieldWithPath("totalAmount").description("??? ??????"),
                                fieldWithPath("orderName").description("?????? ???"),
                                fieldWithPath("orderId").description("?????? ?????????")

                        )));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ??????")
    void getOrderAndPaymentInfo() throws Exception {
        when(orderService.getOrderAndPaymentInfo(anyString()))
                .thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(
                                url + "/payment/{orderId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderName").value(dto.getOrderName()))
                .andExpect(jsonPath("$.address").value(dto.getAddress()))
                .andExpect(jsonPath("$.recipient").value(dto.getRecipient()))
                .andExpect(jsonPath("$.receiveDate").value(dto.getReceiveDate()))
                .andExpect(jsonPath("$.totalAmount").value(dto.getTotalAmount()))
                .andExpect(jsonPath("$.savePoint").value(dto.getSavePoint()))
                .andExpect(jsonPath("$.cardCompany").value(dto.getCardCompany()))
                .andExpect(jsonPath("$.receiptUrl").value(dto.getReceiptUrl()))
                .andDo(document("order-payment-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("orderName").description("?????????"),
                                fieldWithPath("address").description("??????"),
                                fieldWithPath("recipient").description("?????????"),
                                fieldWithPath("receiveDate").description("?????????"),
                                fieldWithPath("totalAmount").description("??? ??????"),
                                fieldWithPath("savePoint").description("?????? ?????????"),
                                fieldWithPath("cardCompany").description("?????? ??????"),
                                fieldWithPath("receiptUrl").description("????????? ??????")
                        )));
    }


    @Test
    @DisplayName("?????? ??? ????????? ?????? ????????? ???????????? ????????? ?????????")
    void getOrderConfirmInfo() throws Exception {
        GetOrderConfirmResponseDto dto = new GetOrderConfirmResponseDto(
                "orderName",
                "buyerName",
                "recipientName",
                "roadAddress",
                "detailAddress",
                LocalDateTime.of(1998, 10, 8, 0, 0),
                "orderRequest",
                10000L,
                "orderId",
                "orderState"
        );

        when(orderService.getOrderConfirmInfo(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/orders/{orderNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderName").value("orderName"))
                .andExpect(jsonPath("$.buyerName").value("buyerName"))
                .andExpect(jsonPath("$.recipientName").value("recipientName"))
                .andExpect(jsonPath("$.addressBase").value("roadAddress"))
                .andExpect(jsonPath("$.addressDetail").value("detailAddress"))
                .andExpect(jsonPath("$.receivedAt").value("1998-10-08T00:00:00"))
                .andExpect(jsonPath("$.orderRequest").value("orderRequest"))
                .andExpect(jsonPath("$.totalAmount").value(10000L))
                .andExpect(jsonPath("$.orderId").value("orderId"))
                .andExpect(jsonPath("$.orderState").value("orderState"))
                .andDo(document("order-confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderNo").description("????????????")
                        ),
                        responseFields(
                                fieldWithPath("orderName").description("?????????"),
                                fieldWithPath("buyerName").description("????????? ??????"),
                                fieldWithPath("recipientName").description("????????? ??????"),
                                fieldWithPath("addressBase").description("???????????????"),
                                fieldWithPath("addressDetail").description("????????????"),
                                fieldWithPath("receivedAt").description("???????????????"),
                                fieldWithPath("orderRequest").description("?????? ??? ????????????"),
                                fieldWithPath("totalAmount").description("?????? ??????"),
                                fieldWithPath("orderId").description("?????? ?????????"),
                                fieldWithPath("orderState").description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ????????? ???????????????.")
    void getOrderDetail_unAuth_user() throws Exception {
        when(orderService.getOrderDetailByOrderId(anyString()))
                .thenReturn(detailDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get(
                "/api/orders/non/{orderId}", 1L)
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
                .andExpect(jsonPath("$.couponAmount").value(order.getCouponDiscount()))
                .andExpect(jsonPath("$.totalAmount").value(order.getOrderPrice()))
                .andExpect(jsonPath("$.orderName").value(order.getOrderName()))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()))
                .andDo(print())
                .andDo(document("non-order-detail",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("orderNo").description("????????????"),
                                fieldWithPath("memberNo").description("????????????"),
                                fieldWithPath("orderProducts").description("????????????"),
                                fieldWithPath("orderState").description("????????????"),
                                fieldWithPath("buyerName").description("?????????"),
                                fieldWithPath("buyerNumber").description("????????? ??????"),
                                fieldWithPath("recipientName").description("?????????"),
                                fieldWithPath("recipientNumber").description("????????? ??????"),
                                fieldWithPath("addressBase").description("?????? ??????"),
                                fieldWithPath("addressDetail").description("?????? ??????"),
                                fieldWithPath("createdAt").description("?????????"),
                                fieldWithPath("receivedAt").description("?????????"),
                                fieldWithPath("invoiceNo").description("?????? ??????"),
                                fieldWithPath("packaged").description("????????????"),
                                fieldWithPath("packageAmount").description("?????????"),
                                fieldWithPath("deliveryAmount").description("?????????"),
                                fieldWithPath("orderRequest").description("????????????"),
                                fieldWithPath("pointAmount").description("????????? ?????????"),
                                fieldWithPath("couponAmount").description("?????? ?????? ??????"),
                                fieldWithPath("totalAmount").description("??? ??????"),
                                fieldWithPath("orderName").description("?????? ???"),
                                fieldWithPath("orderId").description("?????? ?????????")

                        )));

    }


    @Test
    @DisplayName("???????????? ????????? ?????????????????? ????????? ?????????.")
    void change_confirm_state_orderProduct() throws Exception {
        doNothing().when(orderService).confirmOrderProduct(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/token/orders/order-product/{orderProductNo}/member/{memberNo}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("order-state-change-confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderProductNo").description("??????????????????"),
                                parameterWithName("memberNo").description("????????????")
                        )));
    }

    @Test
    @DisplayName("???????????? ??????????????? ?????? ???????????? ??????????????? ?????????")
    void change_exchange_state_orderProduct() throws Exception {
        doNothing().when(orderService).confirmExchange(anyString());

        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/token/orders/order-product/{orderProductNo}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("order-state-change-exchange",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("orderProductNo").description("??????????????????")
                        )));
    }
}