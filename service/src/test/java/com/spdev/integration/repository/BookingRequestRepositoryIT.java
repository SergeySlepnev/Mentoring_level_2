package com.spdev.integration.repository;

import com.spdev.dto.BookingRequestFilter;
import com.spdev.entity.enums.Status;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.BookingRequestRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class BookingRequestRepositoryIT extends IntegrationTestBase {

    private static final Long EXISTING_REQUEST_ID = 1L;
    private static final Integer NO_PREDICATE_PAGE_SIZE = 3;
    private static final Integer PREDICATE_PAGE_SIZE = 2;

    private final EntityManager entityManager;
    private final BookingRequestRepository requestRepository;

    @Test
    void checkFindAllByFilterWithNoPredicates() {
        var noPredicateFilter = BookingRequestFilter.builder().build();
        var noPredicateRequests = requestRepository.findAllByFilter(noPredicateFilter, NO_PREDICATE_PAGE_SIZE);

        assertThat(noPredicateRequests.getTotalPages()).isEqualTo(3);
        assertThat(noPredicateRequests).hasSize(9);

        var requestCreatedDates = noPredicateRequests.stream().map(request -> request.getCreatedAt().toLocalDate().toString()).toList();
        assertThat(requestCreatedDates).containsExactly(
                "2022-10-18",
                "2022-10-17",
                "2022-10-16",
                "2022-10-15",
                "2022-10-14",
                "2022-10-13",
                "2022-10-12",
                "2022-10-11",
                "2022-10-10");
    }

    @Test
    void checkFindAllFilteredByCreatedAtFromToHotelNameStatus() {
        var availableCostFromToFilter = BookingRequestFilter.builder()
                .createdAtFrom(LocalDateTime.of(2022, 10, 11, 0, 0))
                .createdAtTo(LocalDateTime.of(2022, 10, 17, 0, 0))
                .hotelName("PiterPlaza")
                .status(Status.APPROVED)
                .build();

        var filteredRequests = requestRepository.findAllByFilter(availableCostFromToFilter, PREDICATE_PAGE_SIZE);

        assertThat(filteredRequests.getTotalPages()).isEqualTo(1);
        assertThat(filteredRequests).hasSize(2);
    }

    @Test
    void checkUpdate() {
        var existingRequest = requestRepository.findById(EXISTING_REQUEST_ID);
        assertThat(existingRequest).isPresent();

        existingRequest.ifPresent(request -> {
            request.setStatus(Status.PAID);
            request.setCheckOut(LocalDate.of(2022, 11, 22));
            requestRepository.update(request);
        });

        entityManager.clear();
        var updatedRequest = requestRepository.findById(EXISTING_REQUEST_ID);

        updatedRequest.ifPresent(request ->
                assertAll(
                        () -> assertThat(request.getStatus()).isEqualTo(Status.PAID),
                        () -> assertThat(request.getCheckOut()).isEqualTo(LocalDate.of(2022, 11, 22))
                ));
    }
}