package com.spdev.integration.entity;

import com.spdev.entity.Review;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReviewIT {

    private static final Integer EXISTING_REVIEW_1 = 1;
    private static final Integer NONEXISTENT_REVIEW_ID = 999_999;

    @Test
    void shouldSaveValidReviewInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            var actualReview = session.save(validReview);

            assertThat(actualReview).isNotNull();
            assertThat(actualReview).isEqualTo(EXISTING_REVIEW_1);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveIfReviewInvalid() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidReview = TestEntityUtil.getInvalidReview();

            assertThrows(PropertyValueException.class, () -> session.save(invalidReview));
        }
    }

    @Test
    void shouldReturnNullIfReviewIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualReview = session.get(Review.class, NONEXISTENT_REVIEW_ID);

            assertThat(actualReview).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingReview() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            session.save(validReview);
            var expectedReview = session.get(Review.class, EXISTING_REVIEW_1);
            expectedReview.setDate(LocalDateTime.of(2021, 5, 10, 18, 25));
            expectedReview.setDescription("testDescription");
            session.update(expectedReview);
            session.flush();
            session.clear();
            var actualReview = session.get(Review.class, EXISTING_REVIEW_1);

            assertThat(actualReview.getDate()).isEqualTo(LocalDateTime.of(2021, 5, 10, 18, 25));
            assertThat(actualReview.getDescription()).isEqualTo("testDescription");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfReviewNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new Review()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingReview() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            session.save(validUser);
            session.save(validHotel);
            session.save(validReview);
            var savedReview = session.save(validReview);
            assertThat(savedReview).isNotNull();
            session.delete(validReview);
            session.flush();
            session.clear();
            var actualReview = session.get(Review.class, EXISTING_REVIEW_1);

            assertThat(actualReview).isNull();
            session.getTransaction().commit();
        }
    }
}