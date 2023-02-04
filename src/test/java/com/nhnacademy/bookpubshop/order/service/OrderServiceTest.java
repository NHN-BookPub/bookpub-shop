package com.nhnacademy.bookpubshop.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.bookpubshop.address.repository.AddressRepository;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.coupon.dummy.CouponDummy;
import com.nhnacademy.bookpubshop.coupon.entity.Coupon;
import com.nhnacademy.bookpubshop.coupon.exception.NotFoundCouponException;
import com.nhnacademy.bookpubshop.coupon.repository.CouponRepository;
import com.nhnacademy.bookpubshop.couponpolicy.dummy.CouponPolicyDummy;
import com.nhnacademy.bookpubshop.couponpolicy.entity.CouponPolicy;
import com.nhnacademy.bookpubshop.couponstatecode.dummy.CouponStateCodeDummy;
import com.nhnacademy.bookpubshop.couponstatecode.entity.CouponStateCode;
import com.nhnacademy.bookpubshop.coupontemplate.dummy.CouponTemplateDummy;
import com.nhnacademy.bookpubshop.coupontype.dummy.CouponTypeDummy;
import com.nhnacademy.bookpubshop.coupontype.entity.CouponType;
import com.nhnacademy.bookpubshop.file.dummy.FileDummy;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.order.dto.request.CreateOrderRequestDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderDetailResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListForAdminResponseDto;
import com.nhnacademy.bookpubshop.order.dto.response.GetOrderListResponseDto;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.exception.OrderNotFoundException;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductRepository;
import com.nhnacademy.bookpubshop.order.relationship.repository.OrderProductStateCodeRepository;
import com.nhnacademy.bookpubshop.order.repository.OrderRepository;
import com.nhnacademy.bookpubshop.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.repository.OrderStateCodeRepository;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.pricepolicy.exception.NotFoundPricePolicyException;
import com.nhnacademy.bookpubshop.pricepolicy.repository.PricePolicyRepository;
import com.nhnacademy.bookpubshop.product.dto.response.GetProductListForOrderResponseDto;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.NotFoundStateCodeException;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.OrderProductState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 주문 서비스 테스트.
 *
 * @author : 여운석
 * @since : 1.0
 **/
class OrderServiceTest {
    OrderService orderService;
    OrderRepository orderRepository;
    MemberRepository memberRepository;
    PricePolicyRepository pricePolicyRepository;
    AddressRepository addressRepository;
    OrderStateCodeRepository orderStateCodeRepository;
    OrderProductRepository orderProductRepository;
    ProductRepository productRepository;
    OrderProductStateCodeRepository orderProductStateCodeRepository;
    CouponRepository couponRepository;

    Member member;
    BookPubTier bookPubTier;
    PricePolicy deliveryPricePolicy;
    PricePolicy packagePricePolicy;
    Product product;
    BookpubOrder order;
    OrderProduct orderProduct;
    OrderStateCode orderStateCode;
    ProductPolicy productPolicy;
    Coupon coupon;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    OrderProductStateCode orderProductStateCode;
    GetOrderDetailResponseDto detailDto;
    GetProductListForOrderResponseDto productListDto;
    List<GetProductListForOrderResponseDto> productList = new ArrayList<>();
    CreateOrderRequestDto requestDto;
    GetOrderListResponseDto listResponse;
    Map<Long, Long> amounts;
    Map<Long, Long> couponAmount;
    Map<Long, Integer> productCount;
    Map<Long, Long> productSaleAmount;


    @BeforeEach
    void setUp() {
        orderRepository = Mockito.mock(OrderRepository.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        pricePolicyRepository = Mockito.mock(PricePolicyRepository.class);
        addressRepository = Mockito.mock(AddressRepository.class);
        orderStateCodeRepository = Mockito.mock(OrderStateCodeRepository.class);
        orderProductRepository = Mockito.mock(OrderProductRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        orderProductStateCodeRepository = Mockito.mock(OrderProductStateCodeRepository.class);
        couponRepository = Mockito.mock(CouponRepository.class);


        orderService = new OrderServiceImpl(
                orderRepository,
                memberRepository,
                pricePolicyRepository,
                orderStateCodeRepository,
                orderProductRepository,
                productRepository,
                orderProductStateCodeRepository,
                couponRepository
        );

        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        ReflectionTestUtils.setField(member, "memberNo", 1L);
        deliveryPricePolicy = PricePolicyDummy.dummy();
        packagePricePolicy = PricePolicyDummy.dummy();
        orderStateCode = OrderStateCodeDummy.dummy();
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        order = OrderDummy.dummy2(member, deliveryPricePolicy, packagePricePolicy, orderStateCode);
        CouponPolicy policy = CouponPolicyDummy.dummy();
        CouponType type = CouponTypeDummy.dummy();
        Category category = CategoryDummy.dummy();
        CouponStateCode state = CouponStateCodeDummy.dummy();
        coupon = CouponDummy.dummy(
                CouponTemplateDummy.dummy(policy, type, product, category, state),
                order,
                orderProduct,
                member);
        orderProductStateCode = new OrderProductStateCode(
                null,
                OrderProductState.COMPLETE_PAYMENT.getName(),
                true,
                "info");
        product = ProductDummy.dummy(productPolicy,
                productTypeStateCode,
                productSaleStateCode,
                null,
                Collections.EMPTY_LIST);

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

        orderProduct = new OrderProduct(null, product, order, orderProductStateCode,
                3, 1000L, 30000L, "reason");

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

        productListDto = new GetProductListForOrderResponseDto(1L,
                product.getTitle(), product.getFiles().get(0).getFilePath(), product.getSalesPrice(),
                orderProduct.getProductAmount(), orderProductStateCode.getCodeName());

        productList.add(productListDto);

        detailDto.addProducts(productList);
        requestDto = new CreateOrderRequestDto();

        listResponse = new GetOrderListResponseDto(
                order.getOrderNo(),
                order.getOrderStateCode().getCodeName(),
                order.getCreatedAt(),
                order.getReceivedAt(),
                order.getInvoiceNumber(),
                order.getOrderPrice()
        );

        amounts = new HashMap<>();
        couponAmount = new HashMap<>();
        productCount = new HashMap<>();
        productSaleAmount = new HashMap<>();

        amounts.put(1L, 3L);
        couponAmount.put(1L, 2000L);
        productCount.put(1L, 1);
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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);
    }

    @Test
    @DisplayName("주문상세 조회 성공")
    void getOrderDetailById() {
        when(productRepository.getProductListByOrderNo(anyLong()))
                .thenReturn(productList);

        when(orderRepository.getOrderDetailById(anyLong()))
                .thenReturn(Optional.of(detailDto));

        assertThat(orderService.getOrderDetailById(1L).getOrderState())
                .isEqualTo(detailDto.getOrderState());
        assertThat(orderService.getOrderDetailById(1L).getTotalAmount())
                .isEqualTo(detailDto.getTotalAmount());
        assertThat(orderService.getOrderDetailById(1L).getAddressDetail())
                .isEqualTo(detailDto.getAddressDetail());
        assertThat(orderService.getOrderDetailById(1L).getCouponAmount())
                .isEqualTo(detailDto.getCouponAmount());
        assertThat(orderService.getOrderDetailById(1L).getPackageAmount())
                .isEqualTo(detailDto.getPackageAmount());
        assertThat(orderService.getOrderDetailById(1L).getInvoiceNo())
                .isEqualTo(detailDto.getInvoiceNo());
        assertThat(orderService.getOrderDetailById(1L).getDeliveryAmount())
                .isEqualTo(detailDto.getDeliveryAmount());
        assertThat(orderService.getOrderDetailById(1L).getBuyerNumber())
                .isEqualTo(detailDto.getBuyerNumber());
        assertThat(orderService.getOrderDetailById(1L).getRecipientName())
                .isEqualTo(detailDto.getRecipientName());
        assertThat(orderService.getOrderDetailById(1L).getRecipientNumber())
                .isEqualTo(detailDto.getRecipientNumber());
        assertThat(orderService.getOrderDetailById(1L).getBuyerName())
                .isEqualTo(detailDto.getBuyerName());
        assertThat(orderService.getOrderDetailById(1L).getOrderRequest())
                .isEqualTo(detailDto.getOrderRequest());
        assertThat(orderService.getOrderDetailById(1L).getCreatedAt())
                .isEqualTo(detailDto.getCreatedAt());
        assertThat(orderService.getOrderDetailById(1L).getCouponAmount())
                .isEqualTo(detailDto.getCouponAmount());
    }

    @Test
    @DisplayName("주문상세 조회 실패")
    void getOrderDetailByIdFailed() {
        when(productRepository.getProductListByOrderNo(anyLong()))
                .thenReturn(productList);

        when(orderRepository.getOrderDetailById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderDetailById(1L)).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("주문 등록 성공")
    void createOrder() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(deliveryPricePolicy));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(packagePricePolicy));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(orderRepository.save(any()))
                .thenReturn(order);

        Long orderNo = orderService.createOrder(requestDto);

        assertThat(orderNo).isEqualTo(1L);

        verify(orderRepository, times(1))
                .save(any());
        verify(orderProductRepository, times(1))
                .save(any());
    }

    @Test
    @DisplayName("주문 등록 실패(상품 번호가 잘못 입력됨)")
    void createOrderFailed() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(deliveryPricePolicy));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(packagePricePolicy));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(orderRepository.save(any()))
                .thenReturn(order);

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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);

        assertThatThrownBy(() -> orderService.createOrder(requestDto))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("주문 등록 실패(배송,포장 정책 번호가 잘못 입력됨)")
    void createOrderFailed_pricePolicy() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.empty());
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(orderRepository.save(any()))
                .thenReturn(order);

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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);

        assertThatThrownBy(() -> orderService.createOrder(requestDto))
                .isInstanceOf(NotFoundPricePolicyException.class)
                .hasMessage(NotFoundPricePolicyException.MESSAGE);
    }

    @Test
    @DisplayName("주문 등록 실패(주문코드 번호가 잘못 입력됨)")
    void createOrderFailed_orderState() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(packagePricePolicy));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.empty());
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(orderRepository.save(any()))
                .thenReturn(order);

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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);

        assertThatThrownBy(() -> orderService.createOrder(requestDto))
                .isInstanceOf(NotFoundStateCodeException.class)
                .hasMessage(NotFoundStateCodeException.MESSAGE);
    }

    @Test
    @DisplayName("주문 등록 실패(주문상품코드 번호가 잘못 입력됨)")
    void createOrderFailed_orderProductState() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(packagePricePolicy));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.empty());
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.of(coupon));
        when(orderRepository.save(any()))
                .thenReturn(order);

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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);

        assertThatThrownBy(() -> orderService.createOrder(requestDto))
                .isInstanceOf(NotFoundStateCodeException.class)
                .hasMessage(NotFoundStateCodeException.MESSAGE);
    }

    @Test
    @DisplayName("주문 등록 실패(쿠폰 번호가 잘못 입력됨)")
    void createOrderFailed_coupon() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(pricePolicyRepository.getPricePolicyById(anyInt()))
                .thenReturn(Optional.of(packagePricePolicy));
        when(orderStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderStateCode));
        when(orderProductStateCodeRepository.findByCodeName(anyString()))
                .thenReturn(Optional.of(orderProductStateCode));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));
        when(couponRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        when(orderRepository.save(any()))
                .thenReturn(order);

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
        ReflectionTestUtils.setField(requestDto, "productCount", productCount);
        ReflectionTestUtils.setField(requestDto, "productSaleAmount", productSaleAmount);
        ReflectionTestUtils.setField(requestDto, "memberNo", 1L);
        ReflectionTestUtils.setField(requestDto, "deliveryFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "packingFeePolicyNo", 1);
        ReflectionTestUtils.setField(requestDto, "savePoint", 100L);

        assertThatThrownBy(() -> orderService.createOrder(requestDto))
                .isInstanceOf(NotFoundCouponException.class)
                .hasMessageContaining(NotFoundCouponException.MESSAGE);
    }

    @Test
    @DisplayName("송장번호 수정 성공")
    void modifyInvoiceNumber() {
        when(orderRepository.findById(order.getOrderNo()))
                .thenReturn(Optional.of(order));

        orderService.modifyInvoiceNumber(order.getOrderNo(), "1231231231");

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("송장번호 수정 성공")
    void modifyInvoiceNumberFailed() {
        when(orderRepository.findById(order.getOrderNo()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.modifyInvoiceNumber(order.getOrderNo(), "1231231231"))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("상태코드 수정 성공")
    void modifyStateCode() {
        when(orderRepository.findById(order.getOrderNo()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(orderStateCode.getCodeName()))
                .thenReturn(Optional.of(order.getOrderStateCode()));

        orderService.modifyStateCode(orderStateCode.getCodeName(), order.getOrderNo());

        verify(orderRepository, times(1))
                .save(order);
    }

    @Test
    @DisplayName("상태코드 수정 실패")
    void modifyStateCodeFailedOrder() {
        when(orderRepository.findById(order.getOrderNo()))
                .thenReturn(Optional.empty());
        when(orderStateCodeRepository.findByCodeName(orderStateCode.getCodeName()))
                .thenReturn(Optional.of(order.getOrderStateCode()));

        assertThatThrownBy(() -> orderService.modifyStateCode(orderStateCode.getCodeName(), order.getOrderNo()))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("상태코드 수정 실패")
    void modifyStateCodeFailedProduct() {
        when(orderRepository.findById(order.getOrderNo()))
                .thenReturn(Optional.of(order));
        when(orderStateCodeRepository.findByCodeName(orderStateCode.getCodeName()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.modifyStateCode(orderStateCode.getCodeName(), order.getOrderNo()))
                .isInstanceOf(NotFoundStateCodeException.class);
    }


    @Test
    @DisplayName("전체 주문 조회 성공")
    void getOrderList() {
        List<GetOrderListForAdminResponseDto> orders = new ArrayList<>();

        GetOrderListForAdminResponseDto adminResponseDto = new GetOrderListForAdminResponseDto(
                order.getOrderNo(),
                order.getMember().getMemberId(),
                order.getCreatedAt(),
                order.getInvoiceNumber(),
                order.getOrderStateCode().getCodeName(),
                order.getOrderPrice(),
                order.getReceivedAt());

        orders.add(adminResponseDto);

        Pageable pageable = Pageable.ofSize(10);
        Page<GetOrderListForAdminResponseDto> pages = PageableExecutionUtils.getPage(orders, pageable, orders::size);

        when(productRepository.getProductListByOrderNo(order.getOrderNo()))
                .thenReturn(List.of(productListDto));
        when(orderRepository.getOrdersList(pageable))
                .thenReturn(pages);


        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getMemberId()).isEqualTo(order.getMember().getMemberId());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getOrderState()).isEqualTo(order.getOrderStateCode().getCodeName());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getTotalAmount()).isEqualTo(order.getOrderPrice());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getReceivedAt()).isEqualTo(order.getReceivedAt());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getCreatedAt()).isEqualTo(order.getCreatedAt());
        assertThat(orderService.getOrderList(pageable).getContent()
                .get(0).getInvoiceNo()).isEqualTo(order.getInvoiceNumber());
    }

    @Test
    @DisplayName("멤버 번호로 주문 전체 조회 성공")
    void getOrderListByUsers() {
        List<GetOrderListResponseDto> orders = new ArrayList<>();
        listResponse.addOrderProducts(List.of(productListDto));
        orders.add(listResponse);

        Pageable pageable = Pageable.ofSize(10);
        Page<GetOrderListResponseDto> pages = PageableExecutionUtils.getPage(orders, pageable, orders::size);

        when(productRepository.getProductListByOrderNo(order.getOrderNo()))
                .thenReturn(List.of(productListDto));
        when(orderRepository.getOrdersListByUser(pageable, member.getMemberNo()))
                .thenReturn(pages);

        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderProducts().get(0).getProductNo()).isEqualTo(productListDto.getProductNo());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderProducts().get(0).getProductAmount()).isEqualTo(productListDto.getProductAmount());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderProducts().get(0).getSalesPrice()).isEqualTo(productListDto.getSalesPrice());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderProducts().get(0).getTitle()).isEqualTo(productListDto.getTitle());

        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getOrderState()).isEqualTo(order.getOrderStateCode().getCodeName());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getTotalAmount()).isEqualTo(order.getOrderPrice());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getReceivedAt()).isEqualTo(order.getReceivedAt());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getCreatedAt()).isEqualTo(order.getCreatedAt());
        assertThat(orderService.getOrderListByUsers(pageable, member.getMemberNo())
                .getContent().get(0).getInvoiceNo()).isEqualTo(order.getInvoiceNumber());
    }
}