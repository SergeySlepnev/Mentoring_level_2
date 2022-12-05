package com.spdev.repository;

import com.spdev.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HotelRepository extends
        JpaRepository<Hotel, Integer>,
        FilterHotelRepository,
        QuerydslPredicateExecutor<Hotel> {

}