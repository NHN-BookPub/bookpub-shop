package com.nhnacademy.bookpubshop.wishlist.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.nhnacademy.bookpubshop.member.dummy.MemberDummy;
import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.dummy.ProductDummy;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductPolicyDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductSaleStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.dummy.ProductTypeStateCodeDummy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductPolicy;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductSaleStateCode;
import com.nhnacademy.bookpubshop.product.relationship.entity.ProductTypeStateCode;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.tier.dummy.TierDummy;
import com.nhnacademy.bookpubshop.tier.entity.BookPubTier;
import com.nhnacademy.bookpubshop.wishlist.dto.request.CreateWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.DeleteWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.ModifyWishlistAlarmRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import com.nhnacademy.bookpubshop.wishlist.exception.WishlistNotFoundException;
import com.nhnacademy.bookpubshop.wishlist.repository.WishlistRepository;
import com.nhnacademy.bookpubshop.wishlist.service.impl.WishlistServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * wishlistServiceTest.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@ExtendWith(SpringExtension.class)
@Import(WishlistServiceImpl.class)
class WishlistServiceTest {

    @Autowired
    WishlistService wishlistService;

    @MockBean
    WishlistRepository wishlistRepository;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    ProductRepository productRepository;
    BookPubTier bookPubTier;
    Member member;
    ProductPolicy productPolicy;
    ProductTypeStateCode productTypeStateCode;
    ProductSaleStateCode productSaleStateCode;
    Product product;
    ArgumentCaptor<Wishlist> captor;

    @BeforeEach
    void setUp() {
        captor = ArgumentCaptor.forClass(Wishlist.class);
        bookPubTier = TierDummy.dummy();
        member = MemberDummy.dummy(bookPubTier);
        productPolicy = ProductPolicyDummy.dummy();
        productTypeStateCode = ProductTypeStateCodeDummy.dummy();
        productSaleStateCode = ProductSaleStateCodeDummy.dummy();
        product = ProductDummy.dummy(productPolicy, productTypeStateCode, productSaleStateCode);
    }

    @Test
    @DisplayName("위시리스트 등록 성공 테스트")
    void createWishlist_Success_Test() {
        // given
        CreateWishlistRequestDto dto = new CreateWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        // then
        wishlistService.createWishlist(anyLong(), dto);

        verify(wishlistRepository, times(1))
                .save(captor.capture());
    }

    @Test
    @DisplayName("멤버가 없는 경우 실패 테스트")
    void createWishlist_Fail_Test_NoMember() {
        // given
        CreateWishlistRequestDto dto = new CreateWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> wishlistService.createWishlist(anyLong(), dto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining(MemberNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("멤버가 있지만 상품이 없는 경우 실패 테스트")
    void createWishlist_Fail_Test_NoProduct() {
        // given
        CreateWishlistRequestDto dto = new CreateWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> wishlistService.createWishlist(anyLong(), dto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(ProductNotFoundException.MESSAGE);
    }

    @Test
    @DisplayName("위시리스트 조회 테스트")
    void getWishlistTest() {
        // given
        GetWishlistResponseDto dto = new GetWishlistResponseDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);
        ReflectionTestUtils.setField(dto, "title", "제목");
        ReflectionTestUtils.setField(dto, "productPublisher", "출판사");
        ReflectionTestUtils.setField(dto, "thumbnail", "이미지 path");
        ReflectionTestUtils.setField(dto, "wishlistApplied", false);


        Pageable pageable = Pageable.ofSize(5);
        PageImpl<GetWishlistResponseDto> page = new PageImpl<>(List.of(dto), pageable, 1);

        // when
        when(wishlistRepository.findWishlistByMember(pageable, 1L))
                .thenReturn(page);

        // then
        wishlistService.getWishlist(pageable, 1L);

        verify(wishlistRepository, times(1))
                .findWishlistByMember(pageable, 1L);
    }

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    void deleteWishlist_Test() {
        // given
        DeleteWishlistRequestDto dto = new DeleteWishlistRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when

        // then
        wishlistService.deleteWishlist(1L, dto);

        verify(wishlistRepository, times(1))
                .deleteById(new Wishlist.Pk(1L, dto.getProductNo()));
    }

    @Test
    @DisplayName("알람 상태 변경 성공 테스트")
    void modifyAlarm_Success_Test() {
        // given
        Wishlist wishlist = new Wishlist(new Wishlist.Pk(member.getMemberNo(), product.getProductNo()), member, product, false);
        ModifyWishlistAlarmRequestDto dto = new ModifyWishlistAlarmRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        when(wishlistRepository.findById(new Wishlist.Pk(1L, dto.getProductNo())))
                .thenReturn(Optional.of(wishlist));

        // then
        wishlistService.modifyWishlistAlarm(1L, dto);

        verify(wishlistRepository, times(1))
                .findById(new Wishlist.Pk(1L, 1L));
    }

    @Test
    @DisplayName("알람 상태 변경 실패 테스트")
    void modifyAlarm_Fail_Test() {
        // given
        ModifyWishlistAlarmRequestDto dto = new ModifyWishlistAlarmRequestDto();
        ReflectionTestUtils.setField(dto, "productNo", 1L);

        // when
        when(wishlistRepository.findById(new Wishlist.Pk(1L, 1L)))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> wishlistService.modifyWishlistAlarm(1L, dto))
                .isInstanceOf(WishlistNotFoundException.class)
                .hasMessageContaining(WishlistNotFoundException.MESSAGE);
    }

}