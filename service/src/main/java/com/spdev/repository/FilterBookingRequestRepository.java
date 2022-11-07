package com.spdev.repository;

import com.spdev.dto.BookingRequestFilter;
import com.spdev.entity.BookingRequest;
import org.springframework.data.domain.Page;

public interface FilterBookingRequestRepository {

    Page<BookingRequest> findAllByFilter(BookingRequestFilter filter, Integer pageSize);

    void update(BookingRequest hotel);
}
