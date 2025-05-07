package com.htt.elearning.wishlist.controller;

import com.htt.elearning.wishlist.dto.WishlistDTO;
import com.htt.elearning.wishlist.pojo.Wishlist;
import com.htt.elearning.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/list-wishlist")
    public ResponseEntity<List<Wishlist>> findAllWishlist() {
        return ResponseEntity.ok(wishlistService.findByUserId());
    }

    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(
            @RequestBody WishlistDTO wishlistDTO
    ) {
        Wishlist wishlist = wishlistService.createWishlist(wishlistDTO);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> deleteWishlist(
            @PathVariable Long courseId
    ) {
        wishlistService.deleteWishlist(courseId);
        return ResponseEntity.ok("Delete course in wishlist successfully!!");
    }

}
