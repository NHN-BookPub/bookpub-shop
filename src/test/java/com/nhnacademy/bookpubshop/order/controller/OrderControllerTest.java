package com.nhnacademy.bookpubshop.order.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.bookpubshop.error.ShopAdviceController;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dto.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.service.OrderService;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dto.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.utils.PageResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
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
    List<GetProductListForOrderResponseDto> products = new ArrayList<>();
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    OrderProduct orderProduct;
    OrderProductStateCode orderProductStateCode;
    List<GetOrderListResponseDto> orders = new ArrayList<>();
    Pageable pageable;
    Page<GetOrderListResponseDto> pages;
    String url = "/api/orders";


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
                OrderProductState.COMPLETE.getName(),
                true,
                "info");

        orderProduct = new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");

        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");

        productDto = new GetProductListForOrderResponseDto(1L, product.getProductThumbnail(),
                product.getTitle(), product.getSalesPrice(), orderProduct.getProductAmount());

        products.add(productDto);

        requestDto = new CreateOrderRequestDto();
        Map<Long, Integer> amounts = new HashMap<>();
        amounts.put(1L, 3);
        Map<Long, Long> couponAmount = new HashMap<>();
        couponAmount.put(1L, 2000L);
        Map<Long, String> orderReason = new HashMap<>();
        orderReason.put(1L, "test");
        ReflectionTestUtils.setField(requestDto, "productNos", List.of(1L));
        ReflectionTestUtils.setField(requestDto, "productAmounts", amounts);
        ReflectionTestUtils.setField(requestDto, "productCouponAmounts", couponAmount);
        ReflectionTestUtils.setField(requestDto, "orderProductReasons", orderReason);
        ReflectionTestUtils.setField(requestDto, "orderState", order.getOrderStateCode().getCodeName());
        ReflectionTestUtils.setField(requestDto, "buyerName", order.getOrderBuyer());
        ReflectionTestUtils.setField(requestDto, "buyerNumber", order.getBuyerPhone());
        ReflectionTestUtils.setField(requestDto, "recipientName", order.getOrderRecipient());
        ReflectionTestUtils.setField(requestDto, "recipientNumber", order.getRecipientPhone());
        ReflectionTestUtils.setField(requestDto, "addressDetail", order.getAddressDetail());
        ReflectionTestUtils.setField(requestDto, "roadAddress", order.getRoadAddress());
        ReflectionTestUtils.setField(requestDto, "receivedAt", order.getReceivedAt());
        ReflectionTestUtils.setField(requestDto, "packaged", order.isOrderPackaged());
        ReflectionTestUtils.setField(requestDto, "orderRequest", order.getOrderRequest());
        ReflectionTestUtils.setField(requestDto, "pointAmount", order.getPointAmount());
        ReflectionTestUtils.setField(requestDto, "couponAmount", order.getCouponDiscount());

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

        pageable = Pageable.ofSize(10);

        pages = PageableExecutionUtils.getPage(orders, pageable, orders::size);
    }

    @Test
    @DisplayName("전체 주문 조회 성공")
    void getOrders() throws Exception {
        when(orderService.getOrderList(pageable))
                .thenReturn(new PageResponse<>(pages));

        mockMvc.perform(get(url + "?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new PageResponse<>(pages))))
                .andExpect(status().isOk())
                .andDo(print());

        verify(orderService, times(1)).getOrderList(pageable);

        assertThat(orderService.getOrderList(pageable).getContent().get(0).getInvoiceNo())
                .isEqualTo(listDto.getInvoiceNo());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getOrderState())
                .isEqualTo(listDto.getOrderState());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getOrderProducts().get(0).getProductNo())
                .isEqualTo(listDto.getOrderProducts().get(0).getProductNo());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getOrderNo())
                .isEqualTo(listDto.getOrderNo());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getTotalAmount())
                .isEqualTo(listDto.getTotalAmount());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getCreatedAt())
                .isEqualTo(listDto.getCreatedAt());
        assertThat(orderService.getOrderList(pageable).getContent().get(0).getReceivedAt())
                .isEqualTo(listDto.getReceivedAt());
    }

    @Test
    @DisplayName("주문 등록 성공")
    void createOrder() throws Exception {
        doNothing().when(orderService).createOrder(requestDto, member.getMemberNo());

        mockMvc.perform(post(url)
                .content(mapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(orderService, times(1))
                .createOrder(any(), anyLong());

    }

    @Test
    @DisplayName("송장번호 수정 성공")
    void modifyInvoiceNo() throws Exception {
        doNothing().when(orderService).modifyInvoiceNumber(order.getOrderNo(), "1231231231");

        mockMvc.perform(put(url + "/{orderNo}/invoice?no=1231231231", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(orderService, times(1))
                .modifyInvoiceNumber(any(), anyString());
    }

    @Test
    @DisplayName("상태코드 수정 성공")
    void modifyStateCode() throws Exception {
        doNothing().when(orderService).modifyInvoiceNumber(order.getOrderNo(), "결제완료");

        mockMvc.perform(put(url + "/{orderNo}/state?code=결제완료", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(orderService, times(1))
                .modifyStateCode(anyString(), anyLong());
    }

    @Test
    @DisplayName("멤버 번호로 주문 조회 성공")
    void getOrdersByMember() throws Exception {
        when(orderService.getOrderListByUsers(any(), anyLong()))
                .thenReturn(new PageResponse<>(pages));

        mockMvc.perform(get(url + "/member?page=0&size=10&no=" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(orderService, times(1)).getOrderListByUsers(pageable, 1L);
    }

    @Test
    @DisplayName("주문 상세 조회 성공")
    void getOrderDetailByOrderNo() throws Exception {
        when(orderService.getOrderDetailById(anyLong()))
                .thenReturn(detailDto);

        mockMvc.perform(get(url + "/{orderNo}", 1L)
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
                .andDo(print());
    }
}