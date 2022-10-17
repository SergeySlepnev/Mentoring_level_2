package com.spdev.dao;

import com.spdev.entity.enums.Status;
import com.spdev.util.HibernateTestUtil;
import com.spdev.util.TestDataImporter;
import com.spdev.util.TestEntityUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRequestRepositoryIT {

    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    private final Session session = sessionFactory.getCurrentSession();
    private final UserRepository userRepository = new UserRepository(session);
    private final HotelRepository hotelRepository = new HotelRepository(session);
    private final RoomRepository roomRepository = new RoomRepository(session);
    private final BookingRequestRepository bookingRequestRepository = new BookingRequestRepository(session);

    @BeforeAll
    public static void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public static void finish() {
        sessionFactory.close();
    }

    @Test
    void checkSave() {
        session.beginTransaction();

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

        session.getTransaction().rollback();
    }

    @Test
    void checkDelete() {
        session.beginTransaction();

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

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdate() {
        session.beginTransaction();

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
        session.clear();

        assertThat(savedBookingRequest.getStatus()).isEqualTo(Status.CANCELED);

        session.getTransaction().rollback();
    }

    @Test
    void findById() {
        session.beginTransaction();

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

        session.clear();
        var actualBookingRequest = bookingRequestRepository.findById(savedBookingRequest.getId());

        assertThat(actualBookingRequest).isPresent();

        session.getTransaction().rollback();
    }

    @Test
    void findAll() {
        session.beginTransaction();

        var actualBookingRequest = bookingRequestRepository.findAll();
        assertThat(actualBookingRequest).hasSize(9);

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllByStatusOrderedDescByCreatedAt() {
        session.beginTransaction();

        var actualRequests = bookingRequestRepository.findAllByStatusOrderedDescByCreatedAt(Status.APPROVED);
        assertThat(actualRequests).hasSize(3);

        var daysOfRequests = actualRequests.stream().map(request -> request.getCreatedAt().getDayOfMonth()).toList();
        assertThat(daysOfRequests).containsExactly(16, 13, 12);

        session.getTransaction().rollback();
    }
}