package com.spdev.integration.entity;

import com.spdev.entity.Review;
import com.spdev.entity.enums.Rating;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.spdev.integration.util.TestEntityUtil.FIRST_VALID_REVIEW;
import static com.spdev.integration.util.TestEntityUtil.INVALID_REVIEW;
import static com.spdev.integration.util.TestEntityUtil.SECOND_VALID_REVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Tag("Testing mapping functionality of Review.class")
class ReviewIT extends IntegrationTestBase {

    @Test
    void shouldSaveNewValidReviewInDb() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction in shouldSaveNewValidReviewInDb() was opened {}:", testTransaction);

            log.info("Test valid Review entity is in a transient state {}:", TestEntityUtil.SECOND_VALID_REVIEW);
            var expectedId = session.save(TestEntityUtil.SECOND_VALID_REVIEW);

            assertThat(expectedId).isNotNull();
            assertThat(TestEntityUtil.SECOND_VALID_REVIEW.getId()).isEqualTo(expectedId);
            log.info("Test shouldSaveNewValidReviewInDb() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowSQLExceptionIfReviewInvalid() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var invalidReview = INVALID_REVIEW;
            log.info("Test invalid Review entity is in a transient state {}:", invalidReview);

            assertThrows(ConstraintViolationException.class, () -> session.save(invalidReview));
            log.info("Test shouldThrowSQLExceptionIfReviewInvalid is passed");
        }
    }

    @Test
    void shouldGetExistingReview() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedReview = session.get(Review.class, FIRST_VALID_REVIEW.getId());

            assertThat(expectedReview).isNotNull();
            assertThat(FIRST_VALID_REVIEW).isEqualTo(expectedReview);
            log.info("Test shouldGetExistingReview() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldGetNullIfReviewNotExisting() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedReview = session.get(Review.class, 10);

            assertThat(expectedReview).isNull();
            log.info("Test shouldGetNullIfReviewNotExisting() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingReview() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var expectedReview = session.get(Review.class, FIRST_VALID_REVIEW.getId());
            expectedReview.setHotelId(1);
            expectedReview.setDate(LocalDateTime.of(2021, 10, 7, 12, 35));
            expectedReview.setRating(Rating.FIVE);
            expectedReview.setDescription("Nice hotel!");
            session.update(expectedReview);
            var actualReview = session.get(Review.class, FIRST_VALID_REVIEW.getId());

            assertThat(actualReview).isNotNull();
            assertThat(actualReview).isEqualTo(expectedReview);
            log.info("Test shouldUpdateExistingReview() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionWhileUpdateIfReviewNotExist() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            assertThrows(TransientObjectException.class, () -> session.update(new Review()));
            log.info("Test shouldTrowExceptionWhileUpdateIfReviewNotExist() is passed");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingReview() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var testTransaction = session.beginTransaction();
            log.info("Test transaction was opened {}:", testTransaction);

            var firstExpectedReview = session.save(SECOND_VALID_REVIEW);
            assertThat(firstExpectedReview).isNotNull();
            session.delete(SECOND_VALID_REVIEW);
            var expectedReview = session.get(Review.class, SECOND_VALID_REVIEW.getId());

            assertThat(expectedReview).isNull();
            log.info("Test shouldDeleteExistingReview() is passed");
            session.getTransaction().commit();
        }
    }
}