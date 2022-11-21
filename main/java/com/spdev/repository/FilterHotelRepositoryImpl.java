package com.spdev.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.Hotel;
import com.spdev.filter.HotelFilter;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QHotel.hotel;

@RequiredArgsConstructor
public class FilterHotelRepositoryImpl implements FilterHotelRepository {

    private final EntityManager entityManager;

    @Override
    public List<Hotel> findAllByFilter(HotelFilter filter, Predicate predicate) {
//        var predicate = QPredicates.builder()
//                .add(filter.name(), hotel.name::eq)
//                .add(filter.status(), hotel.status::eq)
//                .add(filter.available(), hotel.available::eq)
//                .add(filter.country(), hotel.hotelDetails.country::containsIgnoreCase)
//                .add(filter.locality(), hotel.hotelDetails.locality::containsIgnoreCase)
//                .add(filter.star(), hotel.hotelDetails.star::eq)
//                .build();

        return new JPAQuery<Hotel>(entityManager)
                .select(hotel)
                .from(hotel)
                .where(hotel.available.eq(true))
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), entityManager.getEntityGraph("Hotel.hotelDetails"))
                .orderBy(hotel.hotelDetails.star.asc())
                .fetch();
    }
}