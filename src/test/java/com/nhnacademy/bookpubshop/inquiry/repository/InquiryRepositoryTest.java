package com.nhnacademy.bookpubshop.inquiry.repository;

import static com.nhnacademy.bookpubshop.state.InquiryState.EXCHANGE;
import static org.assertj.core.api.Assertions.assertThat;
import com.nhnacademy.bookpubshop.category.dummy.CategoryDummy;
import com.nhnacademy.bookpubshop.category.entity.Category;
import com.nhnacademy.bookpubshop.file.entity.File;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquiryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryMemberResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryProductResponseDto;
import com.nhnacademy.bookpubshop.inquiry.dto.response.GetInquirySummaryResponseDto;
import com.nhnacademy.bookpubshop.inquiry.entity.Inquiry;
import com.nhnacademy.bookpubshop.inquirystatecode.entity.InquiryStateCode;
import com.nhnacademy.bookpubshop.inquirystatecode.repository.InquiryStateCodeRepository;
import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.order.dummy.OrderDummy;
import com.nhnacademy.bookpubshop.order.entity.BookpubOrder;
import com.nhnacademy.bookpubshop.order.relationship.dummy.OrderProductDummy;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProduct;
import com.nhnacademy.bookpubshop.order.relationship.entity.OrderProductStateCode;
import com.nhnacademy.bookpubshop.orderstatecode.dummy.OrderStateCodeDummy;
import com.nhnacademy.bookpubshop.orderstatecode.entity.OrderStateCode;
import com.nhnacademy.bookpubshop.pricepolicy.dummy.PricePolicyDummy;
import com.nhnacademy.bookpubshop.pricepolicy.entity.PricePolicy;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductCategory;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductPolicyRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductSaleStateCodeRepository;
import com.nhnacademy.bookpubshop.product.relationship.repository.ProductTypeStateCodeRepository;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.state.FileCategory;
import com.nhnacademy.bookpubshop.state.InquiryState;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 상품문의(inquiry) 레포지토리 테스트.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@DataJpaTest
class InquiryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    InquiryStateCodeRepository inquiryStateCodeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPolicyRepository productPolicyRepository;

    @Autowired
    ProductTypeStateCodeRepository productTypeStateCodeRepository;

    @Autowired
    ProductSaleStateCodeRepository productSaleStateCodeRepository;

    @Autowired
    InquiryRepository inquiryRepository;

    BookPubTier bookPubTier;
    Member member;
    Product product;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    InquiryStateCode inquiryStateCode;
    InquiryStateCode errorStateCode;
    Inquiry inquiry;
    OrderProduct orderProduct;
    BookpubOrder order;
    PricePolicy pricePolicy;
    PricePolicy deliveryPolicy;
    OrderStateCode orderStateCode;
    OrderProductStateCode orderProductStateCode;
    Category category;
    ProductCategory productCategory;
    File file;
    PageRequest pageRequest;


    @BeforeEach
    void setUp() {
        pageRequest = PageRequest.of(0, 10);
        orderProductStateCode = new OrderProductStateCode(null, "name",
                true, "info");
        orderStateCode = OrderStateCodeDummy.dummy();
        deliveryPolicy = new PricePolicy(null, "delivery", 1000L);
        pricePolicy = PricePolicyDummy.dummy();
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);

        entityManager.persist(orderProductStateCode);
        entityManager.persist(orderStateCode);
        entityManager.persist(pricePolicy);
        entityManager.persist(deliveryPolicy);
        entityManager.persist(bookPubTier);
        entityManager.persist(member);

        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);

        category = CategoryDummy.dummy();
        productCategory = new ProductCategory(
                new ProductCategory.Pk(category.getCategoryNo(), product.getProductNo()),
                category, product);

        inquiryStateCode = new InquiryStateCode(null, EXCHANGE.getName(), EXCHANGE.isUsed(), "교환");
        errorStateCode = new InquiryStateCode(null, InquiryState.ERROR.getName(), true, "교환");

        entityManager.persist(errorStateCode);
        inquiryStateCodeRepository.save(inquiryStateCode);

        entityManager.persist(category);
        entityManager.persist(productPolicy);
        entityManager.persist(productTypeStateCode);
        entityManager.persist(productSaleStateCode);
        entityManager.persist(product);
        order = OrderDummy.dummy(member, pricePolicy, deliveryPolicy, orderStateCode);
        entityManager.persist(order);
        entityManager.persist(productCategory);

        inquiry = new Inquiry(null, null, member, product, inquiryStateCode, "title", "content", false, false, null);
        orderProduct = OrderProductDummy.dummy(product, order, orderProductStateCode);
        entityManager.persist(orderProduct);
        entityManager.persist(inquiry);

        file = new File(null, null, null, null,
                null, product, null, FileCategory.PRODUCT_THUMBNAIL.getCategory(),
                "productImagePath", "string", "orgin", "name");
        entityManager.persist(file);
    }

    @Test
    @DisplayName(value = "상품문의(inquiry) 레포지토리 save 테스트")
    void inquirySaveTest() {
        LocalDateTime now = LocalDateTime.now();
        Inquiry inquiry = new Inquiry(null, null, member, product, inquiryStateCode, "title", "content", false, false, null);
        inquiryRepository.save(inquiry);

        Optional<Inquiry> optional = inquiryRepository.findById(inquiry.getInquiryNo());
        assertThat(optional).isPresent();
        assertThat(optional.get().getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(optional.get().getInquiryContent()).isEqualTo(inquiry.getInquiryContent());
        assertThat(optional.get().isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(optional.get().getParentInquiry()).isNull();
        assertThat(optional.get().isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(optional.get().getInquiryStateCode().getInquiryCodeNo()).isEqualTo(inquiryStateCode.getInquiryCodeNo());
        assertThat(optional.get().getCreatedAt()).isAfter(now);

        entityManager.clear();
    }

    @Test
    @DisplayName("회원번호로 상품문의 정보가 존재하는 지알아내는 테스트")
    void existsPurchaseHistoryMemberNo() {
        boolean result = inquiryRepository.existsPurchaseHistoryByMemberNo(inquiry.getMember().getMemberNo(),
                inquiry.getProduct().getProductNo());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("상품에 해당하는 문의들을 조회하는 테스트")
    void findSummaryInquiriesByProductTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<GetInquirySummaryProductResponseDto> result =
                inquiryRepository.findSummaryInquiriesByProduct(pageRequest, inquiry.getProduct().getProductNo());

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(result.getContent().get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.getContent().get(0).getMemberNo()).isEqualTo(inquiry.getMember().getMemberNo());
        assertThat(result.getContent().get(0).getInquiryStateCodeName())
                .isEqualTo(inquiry.getInquiryStateCode().getInquiryCodeName());
        assertThat(result.getContent().get(0).getMemberNickname())
                .isEqualTo(member.getMemberNickname());
        assertThat(result.getContent().get(0).getInquiryTitle()).isEqualTo(inquiry.getInquiryTitle());
        assertThat(result.getContent().get(0).isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(result.getContent().get(0).isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(result.getContent().get(0).getCreatedAt()).isEqualTo(inquiry.getCreatedAt());
    }

    @Test
    @DisplayName("불량 상품문의 조회 테스트 입니다.")
    void findSummaryErrorInquiriesTest() {
        Inquiry errorInquiry = new Inquiry(null, member, product, errorStateCode, "title", "content", true);
        entityManager.persist(errorInquiry);

        PageRequest req = PageRequest.of(0, 10);
        Page<GetInquirySummaryResponseDto> page = inquiryRepository.findSummaryErrorInquiries(req);

        List<GetInquirySummaryResponseDto> result = page.getContent();
        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getInquiryNo()).isEqualTo(errorInquiry.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(errorInquiry.getInquiryStateCode().getInquiryCodeName());
        assertThat(result.get(0).getProductTitle()).isEqualTo(product.getTitle());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(errorInquiry.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(errorInquiry.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(errorInquiry.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(errorInquiry.getCreatedAt());
    }

    @Test
    @DisplayName("모든상품문의에대한 간단한 정보들을 조회테스트")
    void findSummaryInquiries() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<GetInquirySummaryResponseDto> page = inquiryRepository.findSummaryInquiries(pageRequest, null, null, null, null);
        List<GetInquirySummaryResponseDto> result = page.getContent();

        System.out.println(inquiry);
        System.out.println(product);
        System.out.println(member);

        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(inquiry.getInquiryStateCode().getInquiryCodeName());
        assertThat(result.get(0).getMemberNickname()).isEqualTo(member.getMemberNickname());
        assertThat(result.get(0).getProductTitle()).isEqualTo(product.getTitle());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(inquiry.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(inquiry.getCreatedAt());

    }

    @Test
    @DisplayName("회원의 문의 내용을 조회하는 테스트")
    void findMemberInquiriesTest() {
        Page<GetInquirySummaryMemberResponseDto> page = inquiryRepository.findMemberInquiries(pageRequest, member.getMemberNo());
        List<GetInquirySummaryMemberResponseDto> result = page.getContent();
        assertThat(page).isNotEmpty();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(result.get(0).getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.get(0).getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get(0).getInquiryStateCodeName()).isEqualTo(inquiry.getInquiryStateCode().getInquiryCodeName());
        assertThat(result.get(0).getProductTitle()).isEqualTo(product.getTitle());
        assertThat(result.get(0).getProductImagePath()).isEqualTo(file.getFilePath());
        assertThat(result.get(0).getInquiryTitle()).isEqualTo(inquiry.getInquiryTitle());
        assertThat(result.get(0).isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(result.get(0).isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(result.get(0).getCreatedAt()).isEqualTo(inquiry.getCreatedAt());
    }

    @Test
    @DisplayName("문의 번호로 문의를 검색하는 테스트..")
    void findInquiryTest(){
        Optional<GetInquiryResponseDto> result = inquiryRepository.findInquiry(inquiry.getInquiryNo());
        assertThat(result).isPresent();
        assertThat(result.get().getInquiryNo()).isEqualTo(inquiry.getInquiryNo());
        assertThat(result.get().getProductNo()).isEqualTo(product.getProductNo());
        assertThat(result.get().getMemberNo()).isEqualTo(member.getMemberNo());
        assertThat(result.get().getInquiryStateCodeName()).isEqualTo(inquiry.getInquiryStateCode().getInquiryCodeName());
        assertThat(result.get().getProductTitle()).isEqualTo(product.getTitle());
        assertThat(result.get().getInquiryTitle()).isEqualTo(inquiry.getInquiryTitle());
        assertThat(result.get().isInquiryDisplayed()).isEqualTo(inquiry.isInquiryDisplayed());
        assertThat(result.get().isInquiryAnswered()).isEqualTo(inquiry.isInquiryAnswered());
        assertThat(result.get().getCreatedAt()).isEqualTo(inquiry.getCreatedAt());
    }
}