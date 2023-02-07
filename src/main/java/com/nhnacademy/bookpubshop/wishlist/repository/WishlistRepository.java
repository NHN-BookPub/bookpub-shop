package com.nhnacademy.bookpubshop.wishlist.repository;

import com.nhnacademy.bookpubshop.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 위시리스트(wishlist) 레포지토리.
 *
 * @author : 박경서
 * @since : 1.0
 **/
public interface WishlistRepository
        extends JpaRepository<Wishlist, Wishlist.Pk>, WishlistRepositoryCustom {
}
