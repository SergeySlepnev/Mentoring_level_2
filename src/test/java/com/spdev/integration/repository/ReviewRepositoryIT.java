package com.spdev.integration.repository;

import com.spdev.entity.Review;
import com.spdev.entity.enums.Rating;
import com.spdev.integration.annotation.IT;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.ReviewRepository;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestEntityUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
class ReviewRepositoryIT {

    private static final Integer EXISTING_HOTEL_ID = 5;

    private final EntityManager entityManager;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Test
    void checkSave() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validReview = TestEntityUtil.getValidReview();

        validHotel.setOwner(validUser);
        validReview.setHotel(validHotel);
        validReview.setUser(validUser);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var actualReview = reviewRepository.save(validReview);

        assertThat(actualReview.getId()).isNotNull();
        assertThat(actualReview).isEqualTo(validReview);
    }

    @Test
    void checkDelete() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validReview = TestEntityUtil.getValidReview();

        validHotel.setOwner(validUser);
        validReview.setHotel(validHotel);
        validReview.setUser(validUser);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var savedReview = reviewRepository.save(validReview);

        assertThat(savedReview.getId()).isNotNull();

        reviewRepository.delete(savedReview);
        var actualReview = reviewRepository.findById(savedReview.getId());

        assertThat(actualReview).isEmpty();
    }

    @Test
    void checkUpdate() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validReview = TestEntityUtil.getValidReview();

        validHotel.setOwner(validUser);
        validReview.setHotel(validHotel);
        validReview.setUser(validUser);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);

        var savedReview = reviewRepository.save(validReview);
        assertThat(savedReview.getId()).isNotNull();

        savedReview.setRating(Rating.FIVE);
        savedReview.setDescription("Nice Hotel");

        reviewRepository.update(savedReview);
        entityManager.clear();

        assertThat(savedReview.getRating()).isEqualTo(Rating.FIVE);
        assertThat(savedReview.getDescription()).isEqualTo("Nice Hotel");
    }

    @Test
    void findById() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validReview = TestEntityUtil.getValidReview();

        validHotel.setOwner(validUser);
        validReview.setHotel(validHotel);
        validReview.setUser(validUser);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var savedReview = reviewRepository.save(validReview);

        assertThat(savedReview.getId()).isNotNull();

        entityManager.clear();
        var actualReview = reviewRepository.findById(savedReview.getId());

        assertAll(
                () -> assertThat(actualReview).isPresent(),
                () -> assertThat(actualReview.get()).isEqualTo(savedReview)
        );
    }

    @Test
    void findAll() {
        var actualReviews = reviewRepository.findAll();
        assertThat(actualReviews).hasSize(4);
    }

    @Test
    void checkFindAllForHotelOrderedDescByRating() {
        var minskPlaza = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualReviews = reviewRepository.findAllForHotelOrderedDescByRating(minskPlaza);

        assertThat(actualReviews).hasSize(4);

        var ratings = actualReviews.stream().map(Review::getRating).toList();
        assertThat(ratings).containsExactly(Rating.FIVE, Rating.FOUR, Rating.THREE, Rating.TWO);
    }

    @Test
    void checkFindAllForHotelOrderedDescByRatingWithContent() {
        var minskPlaza = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualReviews = reviewRepository.findAllForHotelOrderedDescByRatingWithContent(minskPlaza);

        assertThat(actualReviews).hasSize(4);

        var ratings = actualReviews.stream().map(Review::getRating).toList();
        assertThat(ratings).containsExactly(Rating.FIVE, Rating.FOUR, Rating.THREE, Rating.TWO);
    }
}