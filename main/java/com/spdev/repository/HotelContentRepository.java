package com.spdev.repository;

import com.spdev.entity.HotelContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface HotelContentRepository extends
        JpaRepository<HotelContent, Integer>,
        QuerydslPredicateExecutor<HotelContent> {

    @Query("select hc from HotelContent hc where hc.hotel.id = :hotelId ")
    Optional<HotelContent> findByHotelId(Integer hotelId);
}