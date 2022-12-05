package com.spdev.repository;

import com.spdev.entity.Review;
import com.spdev.filter.ReviewFilter;

import java.util.List;

public interface FilterReviewRepository {

    List<Review> findAllByFilter(ReviewFilter filter, Integer pageSize);
}