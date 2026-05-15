package com.personal.project.repositories;

import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Integer> {

    boolean existsByEmail(
            String email
    );

    Optional<User> findByEmail(
            String email
    );

    List<TransactionHistory>
    findTop5ByUserUserIdOrderByCreatedAtDesc(
            Integer userId
    );
}