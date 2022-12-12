package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Review;
import com.spdev.filter.ReviewFilter;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QReview.review;

@RequiredArgsConstructor
public class FilterReviewRepositoryImpl implements FilterReviewRepository {

    private final EntityManager entityManager;

    @Override
    public List<Review> findAllByFilter(ReviewFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.hotelName(), review.hotel.name::eq)
                .add(filter.ratingFrom(), review.rating::goe)
                .add(filter.ratingTo(), review.rating::loe)
                .build();

        return new JPAQuery<Review>(entityManager)
                .select(review)
                .from(review)
                .where(predicate)
                .orderBy(review.rating.desc())
                .fetch();
    }
}