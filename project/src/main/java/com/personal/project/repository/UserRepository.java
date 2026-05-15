package com.personal.project.repository;

import com.personal.project.entity.User;
import com.personal.project.projection.UserDashboardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(
        path = "users",
        excerptProjection =
                UserDashboardProjection.class
)
public interface UserRepository
        extends JpaRepository<User, Integer> {

    boolean existsByEmail(
            String email
    );

    Optional<User> findByEmail(
            String email
    );
}