package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.BookingRequest;
import com.spdev.entity.enums.Status;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QBookingRequest.bookingRequest;

@Repository
public class BookingRequestRepository extends RepositoryBase<Long, BookingRequest> {

    public BookingRequestRepository(EntityManager entityManager) {
        super(BookingRequest.class, entityManager);
    }

    public List<BookingRequest> findAllByStatusOrderedDescByCreatedAt(Status status) {
        return new JPAQuery<BookingRequest>(getEntityManager())
                .select(bookingRequest)
                .from(bookingRequest)
                .where(bookingRequest.status.eq(status))
                .orderBy(bookingRequest.createdAt.desc())
                .fetch();
    }
}