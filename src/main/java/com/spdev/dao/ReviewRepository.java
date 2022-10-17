package com.spdev.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Hotel;
import com.spdev.entity.Review;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QReview.review;

public class ReviewRepository extends RepositoryBase<Long, Review> {

    public ReviewRepository(EntityManager entityManager) {
        super(Review.class, entityManager);
    }

    public List<Review> findAllForHotelOrderedDescByRating(Hotel hotel) {
        return new JPAQuery<Review>(getEntityManager())
                .select(review)
                .from(review)
                .where(review.hotel.name.eq(hotel.getName()))
                .orderBy(review.rating.desc())
                .fetch();
    }

    public List<Review> findAllForHotelOrderedDescByRatingWithContent(Hotel hotel) {
        return new JPAQuery<Review>(getEntityManager())
                .select(review)
                .from(review)
                .where(review.hotel.hotelContents.isNotEmpty().and(review.hotel.name.eq(hotel.getName())))
                .orderBy(review.rating.desc())
                .setHint(GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("withReviewContent"))
                .fetch();
    }
}