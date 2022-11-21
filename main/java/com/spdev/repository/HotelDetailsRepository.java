package com.spdev.repository;

import com.spdev.entity.HotelDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface HotelDetailsRepository extends
        JpaRepository<HotelDetails, Integer>,
        QuerydslPredicateExecutor<HotelDetails> {

    @Query("select hd from HotelDetails hd where hd.hotel.id = :hotelId")
    Optional<HotelDetails> findBy(Integer hotelId);
}