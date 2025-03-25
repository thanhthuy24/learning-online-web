package com.htt.elearning.user.repository;

import com.htt.elearning.user.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    User getUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<User> searchUsersAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.id = :roleId AND" +
            " (LOWER(u.username) LIKE LOWER(CONCAT('%', :key, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :key, '%')))")
    Page<User> findUsersByRoleId(@Param("roleId") Long roleId, @Param("key") String key, Pageable pageable);

    Optional<User> findByGoogleAccount(String googleId);
}
