package com.spdev.query.criteria;

import com.spdev.entity.Hotel_;
import com.spdev.entity.Review;
import com.spdev.entity.Review_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaReview {

    private static final CriteriaReview INSTANCE = new CriteriaReview();

    /**
     * Find all reviews in descending order of rating
     */

    public List<Review> findAllDescendingOrderedByRating(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Review.class);
        var review = criteria.from(Review.class);
        criteria.select(review).orderBy(cb.desc(review.get(Review_.rating)));

        return session.createQuery(criteria).list();
    }

    /**
     * Find average rating of {hotelName}
     */

    public Double findAvgRatingOfHotel(Session session, String hotelName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Double.class);
        var review = criteria.from(Review.class);
        var hotel = review.join(Review_.hotel);

        criteria.select(cb.avg(review.get(Review_.RATING)))
                .where(cb.equal(hotel.get(Hotel_.name), hotelName));

        return session.createQuery(criteria).uniqueResult();
    }

    public static CriteriaReview getInstance() {
        return INSTANCE;
    }
}