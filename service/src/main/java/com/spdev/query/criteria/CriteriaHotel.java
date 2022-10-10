package com.spdev.query.criteria;

import com.spdev.entity.Hotel;
import com.spdev.entity.HotelDetails_;
import com.spdev.entity.Hotel_;
import com.spdev.entity.enums.Star;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaHotel {

    private static final CriteriaHotel INSTANCE = new CriteriaHotel();

    /**
     * Find all hotels
     */

    public List<Hotel> findAll(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);
        var hotel = criteria.from(Hotel.class);
        criteria.select(hotel);

        return session.createQuery(criteria).list();
    }

    /**
     * Find all hotels with the {star} rating
     */

    public List<Hotel> findAllByStar(Session session, Star star) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);
        var hotel = criteria.from(Hotel.class);

        criteria.select(hotel).where(
                cb.equal(hotel.get(Hotel_.hotelDetails).get(HotelDetails_.star), star));

        return session.createQuery(criteria).list();
    }

    /**
     * Find all hotels in ascending order of star rating and then of hotelName
     */

    public List<Hotel> findAllByStarOrderedByStarThenName(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);
        var hotel = criteria.from(Hotel.class);

        criteria.select(hotel).orderBy(
                cb.asc(hotel.get(Hotel_.hotelDetails).get(HotelDetails_.star)),
                cb.asc(hotel.get(Hotel_.name)));

        return session.createQuery(criteria).list();
    }

    /**
     * Find hotels located in the {locality} with the {star} rating
     */

    public List<Hotel> findAllByLocalityAndStar(Session session, String locality, Star star) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Hotel.class);
        var hotel = criteria.from(Hotel.class);

        criteria.select(hotel).where(
                cb.equal(hotel.get(Hotel_.hotelDetails).get(HotelDetails_.locality), locality),
                cb.equal(hotel.get(Hotel_.hotelDetails).get(HotelDetails_.star), star));

        return session.createQuery(criteria).list();
    }

    public static CriteriaHotel getInstance() {
        return INSTANCE;
    }
}