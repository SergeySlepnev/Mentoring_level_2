package com.spdev.repository;

import com.spdev.entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long>, FilterBookingRequestRepository {

}