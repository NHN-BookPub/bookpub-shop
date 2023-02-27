package com.nhnacademy.bookpubshop.wishlist.service.impl;

import com.nhnacademy.bookpubshop.member.entity.Member;
import com.nhnacademy.bookpubshop.member.exception.MemberNotFoundException;
import com.nhnacademy.bookpubshop.member.repository.MemberRepository;
import com.nhnacademy.bookpubshop.product.entity.Product;
import com.nhnacademy.bookpubshop.product.exception.ProductNotFoundException;
import com.nhnacademy.bookpubshop.product.repository.ProductRepository;
import com.nhnacademy.bookpubshop.wishlist.dto.request.CreateWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.DeleteWishlistRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.request.ModifyWishlistAlarmRequestDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistCountResponseDto;
import com.nhnacademy.bookpubshop.wishlist.dto.response.GetWishlistResponseDto;
import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import com.nhnacademy.bookpubshop.wishlist.exception.WishlistNotFoundException;
import com.nhnacademy.bookpubshop.wishlist.repository.WishlistRepository;
import com.nhnacademy.bookpubshop.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 위시리스트 서비스 구현체.
 *
 * @author : 박경서
 * @since : 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void createWishlist(Long memberNo, CreateWishlistRequestDto request) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(MemberNotFoundException::new);
        Product product = productRepository.findById(request.getProductNo())
                .orElseThrow(ProductNotFoundException::new);

        Wishlist wishlist = new Wishlist(
                new Wishlist.Pk(memberNo, request.getProductNo()),
                member,
                product,
                false);

        wishlistRepository.save(wishlist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetWishlistResponseDto> getWishlist(Pageable pageable, Long memberNo) {
        return wishlistRepository.findWishlistByMember(pageable, memberNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GetWishlistCountResponseDto> getCountWishList(Integer categoryNo,
            Pageable pageable) {
        return wishlistRepository.getCountWishList(categoryNo, pageable);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteWishlist(Long memberNo, DeleteWishlistRequestDto request) {
        wishlistRepository.deleteById(new Wishlist.Pk(memberNo, request.getProductNo()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void modifyWishlistAlarm(Long memberNo, ModifyWishlistAlarmRequestDto request) {
        Wishlist wishlist = wishlistRepository.findById(
                        new Wishlist.Pk(memberNo, request.getProductNo()))
                .orElseThrow(WishlistNotFoundException::new);

        wishlist.modifyWishlistAlarm();
    }
}
