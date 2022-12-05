package com.spdev.repository;

import com.spdev.entity.HotelContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface HotelContentRepository extends
        JpaRepository<HotelContent, Integer>,
        QuerydslPredicateExecutor<HotelContent> {

    List<HotelContent> findByHotelId(Integer hotelId);
}