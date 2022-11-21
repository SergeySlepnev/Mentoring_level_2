package com.spdev.repository;

import com.querydsl.core.types.Predicate;
import com.spdev.entity.Hotel;
import com.spdev.filter.HotelFilter;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface FilterHotelRepository {

    @EntityGraph(attributePaths = "hotelDetails")
    List<Hotel> findAllByFilter(HotelFilter filter, Predicate predicate);
}
