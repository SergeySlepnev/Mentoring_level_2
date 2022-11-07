package com.spdev.repository;

import com.spdev.dto.ReviewFilter;
import com.spdev.entity.Review;
import org.springframework.data.domain.Page;

public interface FilterReviewRepository {

    Page<Review> findAllByFilter(ReviewFilter filter, Integer pageSize);

    void update(Review hotel);
}
