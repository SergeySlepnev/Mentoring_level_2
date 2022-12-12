package com.spdev.repository;

import com.spdev.entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BookingRequestRepository extends
        JpaRepository<BookingRequest, Long>,
        FilterBookingRequestRepository,
        QuerydslPredicateExecutor<BookingRequest> {

}