package com.spdev.integration.query.criteria;

import com.spdev.integration.util.TestDataImporter;
import com.spdev.query.criteria.CriteriaReview;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CriteriaReviewIT {

    private static final SessionFactory SESSION_FACTORY = HibernateTestUtil.buildSessionFactory();

    private final CriteriaReview criteriaReview = CriteriaReview.getInstance();

    @BeforeAll
    static void setUp() {
        TestDataImporter.importData(SESSION_FACTORY);
    }

    @AfterAll
    static void finish() {
        SESSION_FACTORY.close();
    }

    @Test
    void checkFindAllByRating() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualReviews = criteriaReview.findAllDescendingOrderedByRating(session);
            assertThat(actualReviews).hasSize(4);

            var actualRatings = actualReviews.stream().map(review -> review.getRating().ordinal()).toList();
            assertThat(actualRatings).contains(1, 2, 2, 3);

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindAvgRatingOfHotel() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualRating = criteriaReview.findAvgRatingOfHotel(session, "MinskPlaza");
            assertThat(actualRating).isEqualTo(2.0);

            session.getTransaction().rollback();
        }
    }
}