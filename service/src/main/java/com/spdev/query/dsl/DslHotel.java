package com.spdev.query.dsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Hotel;
import com.spdev.entity.QHotelDetails;
import com.spdev.entity.dto.HotelFilter;
import com.spdev.entity.enums.Star;
import com.spdev.predicate.QPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.spdev.entity.QHotel.hotel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DslHotel {

    private static final DslHotel INSTANCE = new DslHotel();

    /**
     * Find all hotels
     */

    public List<Hotel> findAll(Session session) {
        return new JPAQuery<Hotel>(session)
                .select(hotel)
                .from(hotel)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("withHotelContent"))
                .fetch();
    }

    /**
     * Find all hotels with the {star} rating
     */

    public List<Hotel> findAllByStar(Session session, Star star) {
        return new JPAQuery<Hotel>(session)
                .select(hotel)
                .from(hotel)
                .where(QHotelDetails.hotelDetails.star.eq(star))
                .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("withHotelContent"))
                .fetch();
    }

    /**
     * Find all hotels in ascending order of star rating and then of hotelName
     */

    public List<Hotel> findAllByStarOrderedByStarThenName(Session session) {
        return new JPAQuery<Hotel>(session)
                .select(hotel)
                .from(hotel)
                .orderBy(QHotelDetails.hotelDetails.star.asc(), hotel.name.asc())
                .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("withHotelContent"))
                .fetch();
    }

    /**
     * Find hotels located in the {locality} with the {star} rating
     */

    public List<Hotel> findAllByLocalityAndStar(Session session, String locality, Star star) {
        return new JPAQuery<Hotel>(session)
                .select(hotel)
                .from(hotel)
                .where(hotel.hotelDetails.locality.eq(locality).and(hotel.hotelDetails.star.eq(star)))
                .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("withHotelContent"))
                .fetch();
    }

    public static DslHotel getInstance() {
        return INSTANCE;
    }

    /**
     * Find hotel by {locality} and {name}
     */

    public List<Hotel> findByLocalityAndName(Session session, HotelFilter filter) {
        var hotelPredicate = QPredicate.builder()
                .add(filter.getLocality(), hotel.hotelDetails.locality::eq)
                .add(filter.getName(), hotel.name::eq)
                .buildAnd();

        return new JPAQuery<Hotel>(session)
                .select(hotel)
                .from(hotel)
                .where(hotelPredicate)
                .fetch();
    }
}