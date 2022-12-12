package com.spdev.repository;

import com.spdev.entity.BookingRequest;
import com.spdev.filter.BookingRequestFilter;

import java.util.List;

public interface FilterBookingRequestRepository {

    List<BookingRequest> findAllByFilter(BookingRequestFilter filter, Integer pageSize);
}