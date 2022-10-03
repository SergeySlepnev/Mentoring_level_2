package com.spdev.integration.entity;

import com.spdev.entity.ReviewContent;
import com.spdev.integration.util.TestEntityUtil;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReviewContentIT {

    private static final Integer EXISTING_REVIEW_CONTENT_1 = 1;
    private static final Integer NONEXISTENT_REVIEW_CONTENT_ID = 999_999;

    @Test
    void shouldSaveValidReviewContentInDb() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();
            var validReviewContent = TestEntityUtil.getValidReviewContent();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            validReviewContent.setReview(validReview);
            session.save(validUser);
            session.save(validHotel);
            session.save(validReview);
            var actualReviewContent = session.save(validReviewContent);

            assertThat(actualReviewContent).isNotNull();
            assertThat(actualReviewContent).isEqualTo(EXISTING_REVIEW_CONTENT_1);
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldThrowExceptionDuringSaveIfReviewContentInvalid() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var invalidReviewContent = TestEntityUtil.getInvalidReviewContent();

            assertThrows(PropertyValueException.class, () -> session.save(invalidReviewContent));
        }
    }

    @Test
    void shouldReturnNullIfReviewContentIsNotExist() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var actualReviewContent = session.get(ReviewContent.class, NONEXISTENT_REVIEW_CONTENT_ID);

            assertThat(actualReviewContent).isNull();
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldUpdateExistingReviewContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();
            var validReviewContent = TestEntityUtil.getValidReviewContent();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            validReviewContent.setReview(validReview);
            session.save(validUser);
            session.save(validHotel);
            session.save(validReview);
            session.save(validReviewContent);
            var expectedReviewContent = session.get(ReviewContent.class, EXISTING_REVIEW_CONTENT_1);
            expectedReviewContent.setLink("testReviewContentLink.jpg");
            session.update(expectedReviewContent);
            session.flush();
            session.clear();
            var actualReviewContent = session.get(ReviewContent.class, EXISTING_REVIEW_CONTENT_1);

            assertThat(actualReviewContent.getLink()).isEqualTo("testReviewContentLink.jpg");
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldTrowExceptionDuringUpdateIfReviewContentNotExisting() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            assertThrows(TransientObjectException.class, () -> session.update(new ReviewContent()));
            session.getTransaction().commit();
        }
    }

    @Test
    void shouldDeleteExistingReviewContent() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var validUser = TestEntityUtil.getValidUser();
            var validHotel = TestEntityUtil.getValidHotel();
            var validReview = TestEntityUtil.getValidReview();
            var validReviewContent = TestEntityUtil.getValidReviewContent();

            validHotel.setOwner(validUser);
            validReview.setHotel(validHotel);
            validReviewContent.setReview(validReview);
            session.save(validUser);
            session.save(validHotel);
            session.save(validReview);
            session.save(validReviewContent);
            var savedReviewContent = session.save(validReviewContent);
            assertThat(savedReviewContent).isNotNull();
            session.delete(validReviewContent);
            session.flush();
            session.clear();
            var actualReviewContent = session.get(ReviewContent.class, EXISTING_REVIEW_CONTENT_1);

            assertThat(actualReviewContent).isNull();
            session.getTransaction().commit();
        }
    }
}