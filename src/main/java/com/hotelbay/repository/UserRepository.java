package com.hotelbay.repository;

import com.hotelbay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByRole(User.UserRole role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
