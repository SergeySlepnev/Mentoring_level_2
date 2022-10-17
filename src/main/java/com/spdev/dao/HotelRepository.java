package com.spdev.dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Hotel;
import com.spdev.entity.QHotelDetails;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QHotel.hotel;
import static com.spdev.entity.QReview.review;

public class HotelRepository extends RepositoryBase<Integer, Hotel> {

    public HotelRepository(EntityManager entityManager) {
        super(Hotel.class, entityManager);
    }

    public List<Hotel> findAllAvailableByLocality(String locality) {
        return new JPAQuery<Hotel>(getEntityManager())
                .select(hotel)
                .from(hotel)
                .where(hotel.hotelDetails.locality.eq(locality).and(hotel.available.eq(true)))
                .setHint(GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("withHotelContent"))
                .fetch();
    }

    public List<Hotel> findByOwnerEmailAndPhone(String email, String phone) {
        return new JPAQuery<Hotel>(getEntityManager())
                .select(hotel)
                .from(hotel)
                .where(hotel.owner.email.eq(email).and(hotel.owner.phone.eq(phone)))
                .fetch();
    }

    public List<Hotel> findAllByStarOrderedByStarThenName() {
        return new JPAQuery<Hotel>(getEntityManager())
                .select(hotel)
                .from(hotel)
                .orderBy(QHotelDetails.hotelDetails.star.asc(), hotel.name.asc())
                .setHint(GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("withHotelContent"))
                .setHint(GraphSemantic.FETCH.getJpaHintName(), getEntityManager().getEntityGraph("withHotelDetails"))
                .fetch();
    }

    public List<Tuple> findAllInLocalityOrderedDescByRating(String locality) {
        return new JPAQuery<Hotel>(getEntityManager())
                .select(hotel.name, review.rating.castToNum(Integer.class).avg())
                .from(review)
                .where(hotel.hotelDetails.locality.eq(locality))
                .groupBy(hotel.name)
                .fetch();
    }
}