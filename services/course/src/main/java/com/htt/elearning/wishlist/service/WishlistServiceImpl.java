package com.htt.elearning.wishlist.service;

import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.repository.CourseRepository;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import com.htt.elearning.wishlist.dto.WishlistDTO;
import com.htt.elearning.wishlist.pojo.Wishlist;
import com.htt.elearning.wishlist.repository.WishlistRepository;
import com.htt.elearning.wishlist.response.WishlistResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserClient userClient;
    private final HttpServletRequest request;
    private final CourseRepository courseRepository;


    @Override
    public List<Wishlist> findByUserId() {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        return wishlistRepository.findByUserId(userId);
    }


    @Override
    public Wishlist createWishlist(WishlistDTO wishlistDTO) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Course existingCourse = courseRepository.findById(wishlistDTO.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));

        Wishlist existingWishlist = wishlistRepository.findByUserIdAndCourseId(userId, wishlistDTO.getCourseId());
        if (existingWishlist != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course already exists!");
        }

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .course(existingCourse)
                .build();

        Wishlist newWishlist = wishlistRepository.save(wishlist);

        return newWishlist;
    }

    @Override
    public void deleteWishlist(Long courseId) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsernameClient(token);

        Wishlist existingWishlist = wishlistRepository.findByUserIdAndCourseId(userId, courseId);
        wishlistRepository.delete(existingWishlist);
    }
}
