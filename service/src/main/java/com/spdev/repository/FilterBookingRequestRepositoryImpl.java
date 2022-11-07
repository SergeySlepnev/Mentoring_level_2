package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.BookingRequestFilter;
import com.spdev.entity.BookingRequest;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import static com.spdev.entity.QBookingRequest.bookingRequest;

@RequiredArgsConstructor
public class FilterBookingRequestRepositoryImpl implements FilterBookingRequestRepository {

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 3;

    private final EntityManager entityManager;

    @Override
    public Page<BookingRequest> findAllByFilter(BookingRequestFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.createdAtFrom(), bookingRequest.createdAt::goe)
                .add(filter.createdAtTo(), bookingRequest.createdAt::loe)
                .add(filter.hotelName(), bookingRequest.hotel.name::eq)
                .add(filter.userPhone(), bookingRequest.user.phone::eq)
                .add(filter.status(), bookingRequest.status::eq)
                .build();

        var result = new JPAQuery<BookingRequest>(entityManager)
                .select(bookingRequest)
                .from(bookingRequest)
                .where(predicate)
                .orderBy(bookingRequest.createdAt.desc())
                .fetch();

        var request = buildPageRequest(pageSize);
        return new PageImpl<>(result, request, result.size());
    }

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    public void update(BookingRequest request) {
        entityManager.merge(request);
        entityManager.flush();
    }

    private PageRequest buildPageRequest(Integer pageSize) {
        return pageSize != null
                ? PageRequest.of(DEFAULT_PAGE_INDEX, pageSize)
                : PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }
}