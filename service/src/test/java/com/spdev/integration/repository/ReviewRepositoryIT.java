package com.spdev.integration.repository;

import com.spdev.dto.ReviewFilter;
import com.spdev.entity.enums.Rating;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ReviewRepositoryIT extends IntegrationTestBase {

    private static final Integer NO_PREDICATE_PAGE_SIZE = 3;
    private static final Integer PREDICATE_PAGE_SIZE = 1;

    private final ReviewRepository reviewRepository;

    @Test
    void checkFindAllByFilterWithNoPredicates() {
        var noPredicateFilter = ReviewFilter.builder().build();
        var noPredicateReviews = reviewRepository.findAllByFilter(noPredicateFilter, NO_PREDICATE_PAGE_SIZE);

        assertThat(noPredicateReviews.getTotalPages()).isEqualTo(2);
        assertThat(noPredicateReviews).hasSize(4);

        var ratings = noPredicateReviews.stream().map(review -> review.getRating().rating).toList();

        assertThat(ratings).containsExactly(5, 4, 3, 2);
    }

    @Test
    void checkFindAllFilteredByHotelNameRatingFromRatingTo() {
        var hotelRatingFromToFilter = ReviewFilter.builder()
                .hotelName("MinskPlaza")
                .ratingFrom(Rating.THREE)
                .ratingTo(Rating.FIVE)
                .build();

        var filteredReview = reviewRepository.findAllByFilter(hotelRatingFromToFilter, PREDICATE_PAGE_SIZE);

        assertThat(filteredReview.getTotalPages()).isEqualTo(3);
        assertThat(filteredReview).hasSize(3);
    }
}