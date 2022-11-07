package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.HotelFilter;
import com.spdev.entity.Hotel;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static com.spdev.entity.QHotel.hotel;

@RequiredArgsConstructor
public class FilterHotelRepositoryImpl implements FilterHotelRepository {

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 3;

    private final EntityManager entityManager;

    @Override
    public Page<Hotel> findAllByFilter(HotelFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.name(), hotel.name::eq)
                .add(filter.status(), hotel.status::eq)
                .add(filter.available(), hotel.available::eq)
                .add(filter.country(), hotel.hotelDetails.country::containsIgnoreCase)
                .add(filter.locality(), hotel.hotelDetails.locality::containsIgnoreCase)
                .add(filter.star(), hotel.hotelDetails.star::eq)
                .build();

        var result = new JPAQuery<Hotel>(entityManager)
                .select(hotel)
                .from(hotel)
                .where(hotel.available.eq(true))
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), entityManager.getEntityGraph("Hotel.hotelDetails"))
                .orderBy(hotel.hotelDetails.star.asc())
                .fetch();

        var request = buildPageRequest(pageSize);
        return new PageImpl<>(result, request, result.size());
    }

    @Override
    public void update(Hotel hotel) {
        entityManager.merge(hotel);
        entityManager.flush();
    }

    private PageRequest buildPageRequest(Integer pageSize) {
        return pageSize != null
                ? PageRequest.of(DEFAULT_PAGE_INDEX, pageSize)
                : PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }
}