package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.ReviewFilter;
import com.spdev.entity.Review;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static com.spdev.entity.QReview.review;

@RequiredArgsConstructor
public class FilterReviewRepositoryImpl implements FilterReviewRepository {

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 3;

    private final EntityManager entityManager;

    @Override
    public Page<Review> findAllByFilter(ReviewFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.hotelName(), review.hotel.name::eq)
                .add(filter.ratingFrom(), review.rating::goe)
                .add(filter.ratingTo(), review.rating::loe)
                .build();

        var result = new JPAQuery<Review>(entityManager)
                .select(review)
                .from(review)
                .where(predicate)
                .orderBy(review.rating.desc())
                .fetch();

        var request = buildPageRequest(pageSize);
        return new PageImpl<>(result, request, result.size());
    }

    @Override
    public void update(Review review) {
        entityManager.merge(review);
        entityManager.flush();
    }

    private PageRequest buildPageRequest(Integer pageSize) {
        return pageSize != null
                ? PageRequest.of(DEFAULT_PAGE_INDEX, pageSize)
                : PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }
}