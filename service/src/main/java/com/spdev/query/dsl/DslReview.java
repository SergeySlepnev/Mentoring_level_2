package com.spdev.query.dsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.QHotel;
import com.spdev.entity.Review;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.spdev.entity.QReview.review;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DslReview {

    private static final DslReview INSTANCE = new DslReview();

    /**
     * Find all reviews in descending order of rating
     */

    public List<Review> findAllDescendingOrderedByRating(Session session) {
        return new JPAQuery<Review>(session)
                .select(review)
                .from(review)
                .orderBy(review.rating.desc())
                .fetch();
    }

    /**
     * Find average rating of {hotelName}
     */

    public Double findAvgRatingOfHotel(Session session, String hotelName) {
        return new JPAQuery<Double>(session)
                .select(review.rating.castToNum(Integer.class).avg())
                .from(review)
                .where(QHotel.hotel.name.eq(hotelName))
                .fetchOne();
    }

    public static DslReview getInstance() {
        return INSTANCE;
    }
}