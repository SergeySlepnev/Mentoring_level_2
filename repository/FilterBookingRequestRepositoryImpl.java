package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.BookingRequest;
import com.spdev.filter.BookingRequestFilter;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QBookingRequest.bookingRequest;

@RequiredArgsConstructor
public class FilterBookingRequestRepositoryImpl implements FilterBookingRequestRepository {

    private final EntityManager entityManager;

    @Override
    public List<BookingRequest> findAllByFilter(BookingRequestFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.createdAtFrom(), bookingRequest.createdAt::goe)
                .add(filter.createdAtTo(), bookingRequest.createdAt::loe)
                .add(filter.hotelName(), bookingRequest.hotel.name::eq)
                .add(filter.userPhone(), bookingRequest.user.phone::eq)
                .add(filter.status(), bookingRequest.status::eq)
                .build();

        return new JPAQuery<BookingRequest>(entityManager)
                .select(bookingRequest)
                .from(bookingRequest)
                .where(predicate)
                .orderBy(bookingRequest.createdAt.desc())
                .fetch();
    }
}