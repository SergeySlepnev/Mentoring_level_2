package com.spdev.dao;

import com.spdev.entity.Review;
import com.spdev.entity.enums.Rating;
import com.spdev.util.HibernateTestUtil;
import com.spdev.util.TestDataImporter;
import com.spdev.util.TestEntityUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReviewRepositoryIT {

    private static final Integer EXISTING_HOTEL_ID = 5;
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    private final Session session = sessionFactory.getCurrentSession();
    private final HotelRepository hotelRepository = new HotelRepository(session);
    private final UserRepository userRepository = new UserRepository(session);
    private final ReviewRepository reviewRepository = new ReviewRepository(session);

    @BeforeAll
    public static void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public static void finish() {
        sessionFactory.close();
    }

    @Test
    void checkSave() {
        session.beginTransaction();

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

        session.getTransaction().rollback();
    }

    @Test
    void checkDelete() {
        session.beginTransaction();

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

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdate() {
        session.beginTransaction();

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
        session.clear();

        assertThat(savedReview.getRating()).isEqualTo(Rating.FIVE);
        assertThat(savedReview.getDescription()).isEqualTo("Nice Hotel");

        session.getTransaction().rollback();
    }

    @Test
    void findById() {
        session.beginTransaction();

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

        session.clear();
        var actualReview = reviewRepository.findById(savedReview.getId());

        assertAll(
                () -> assertThat(actualReview).isPresent(),
                () -> assertThat(actualReview.get()).isEqualTo(savedReview)
        );

        session.getTransaction().rollback();
    }

    @Test
    void findAll() {
        session.beginTransaction();

        var actualReviews = reviewRepository.findAll();
        assertThat(actualReviews).hasSize(4);

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllForHotelOrderedDescByRating() {
        session.beginTransaction();

        var minskPlaza = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualReviews = reviewRepository.findAllForHotelOrderedDescByRating(minskPlaza);

        assertThat(actualReviews).hasSize(4);

        var ratings = actualReviews.stream().map(Review::getRating).toList();
        assertThat(ratings).containsExactly(Rating.FIVE, Rating.FOUR, Rating.THREE, Rating.TWO);
        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllForHotelOrderedDescByRatingWithContent() {
        session.beginTransaction();

        var minskPlaza = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualReviews = reviewRepository.findAllForHotelOrderedDescByRatingWithContent(minskPlaza);

        assertThat(actualReviews).hasSize(4);

        var ratings = actualReviews.stream().map(Review::getRating).toList();
        assertThat(ratings).containsExactly(Rating.FIVE, Rating.FOUR, Rating.THREE, Rating.TWO);
        session.getTransaction().rollback();
    }
}