package com.htt.elearning.wishlist.service;

import com.htt.elearning.wishlist.dto.WishlistDTO;
import com.htt.elearning.wishlist.pojo.Wishlist;
import com.htt.elearning.wishlist.response.WishlistResponse;

import java.util.List;

public interface WishlistService {
    List<Wishlist> findByUserId();
    Wishlist createWishlist(WishlistDTO wishlistDTO);
    void deleteWishlist(Long courseId);
}
