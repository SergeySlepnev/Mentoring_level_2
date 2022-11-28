package com.spdev.repository;

import com.spdev.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReviewRepository extends
        JpaRepository<Review, Long>,
        FilterReviewRepository,
        QuerydslPredicateExecutor<Review> {

}