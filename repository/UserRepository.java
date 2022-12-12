package com.spdev.repository;

import com.spdev.entity.User;
import com.spdev.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Integer>,
        FilterUserRepository,
        QuerydslPredicateExecutor<User> {

    Optional<User> findByUsername(String username);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update User u set u.status = :status where u.id = :id")
    void changeStatusByUserId(Status status, Integer id);
}