package com.spdev.integration.repository;

import com.spdev.entity.enums.Status;
import com.spdev.integration.annotation.IT;
import com.spdev.repository.BookingRequestRepository;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.RoomRepository;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestEntityUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
class BookingRequestRepositoryIT {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRequestRepository bookingRequestRepository;

    @Test
    void checkSave() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();
        var validBookingRequest = TestEntityUtil.getValidBookingRequest();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        validBookingRequest.setUser(validUser);
        validBookingRequest.setHotel(validHotel);
        validBookingRequest.setRoom(validRoom);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        roomRepository.save(validRoom);
        var actualBookingRequest = bookingRequestRepository.save(validBookingRequest);

        assertThat(actualBookingRequest.getId()).isNotNull();
        assertThat(actualBookingRequest).isEqualTo(validBookingRequest);
    }

    @Test
    void checkDelete() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();
        var validBookingRequest = TestEntityUtil.getValidBookingRequest();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        validBookingRequest.setUser(validUser);
        validBookingRequest.setHotel(validHotel);
        validBookingRequest.setRoom(validRoom);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        roomRepository.save(validRoom);
        var savedBookingRequest = bookingRequestRepository.save(validBookingRequest);

        assertThat(savedBookingRequest.getId()).isNotNull();

        bookingRequestRepository.delete(savedBookingRequest);
        var actualBookingRequest = bookingRequestRepository.findById(savedBookingRequest.getId());

        assertThat(actualBookingRequest).isEmpty();
    }

    @Test
    void checkUpdate() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();
        var validBookingRequest = TestEntityUtil.getValidBookingRequest();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        validBookingRequest.setUser(validUser);
        validBookingRequest.setHotel(validHotel);
        validBookingRequest.setRoom(validRoom);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        roomRepository.save(validRoom);

        var savedBookingRequest = bookingRequestRepository.save(validBookingRequest);
        assertThat(savedBookingRequest.getId()).isNotNull();

        savedBookingRequest.setStatus(Status.CANCELED);

        bookingRequestRepository.update(savedBookingRequest);
        entityManager.clear();

        assertThat(savedBookingRequest.getStatus()).isEqualTo(Status.CANCELED);
    }

    @Test
    void findById() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();
        var validBookingRequest = TestEntityUtil.getValidBookingRequest();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        validBookingRequest.setUser(validUser);
        validBookingRequest.setHotel(validHotel);
        validBookingRequest.setRoom(validRoom);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        roomRepository.save(validRoom);
        var savedBookingRequest = bookingRequestRepository.save(validBookingRequest);

        assertThat(savedBookingRequest.getId()).isNotNull();

        entityManager.clear();
        var actualBookingRequest = bookingRequestRepository.findById(savedBookingRequest.getId());

        assertThat(actualBookingRequest).isPresent();
    }

    @Test
    void findAll() {
        var actualBookingRequest = bookingRequestRepository.findAll();
        assertThat(actualBookingRequest).hasSize(9);
    }

    @Test
    void checkFindAllByStatusOrderedDescByCreatedAt() {
        var actualRequests = bookingRequestRepository.findAllByStatusOrderedDescByCreatedAt(Status.APPROVED);
        assertThat(actualRequests).hasSize(3);

        var daysOfRequests = actualRequests.stream().map(request -> request.getCreatedAt().getDayOfMonth()).toList();
        assertThat(daysOfRequests).containsExactly(16, 13, 12);
    }
}