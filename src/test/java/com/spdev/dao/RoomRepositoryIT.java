package com.spdev.dao;

import com.spdev.entity.Room;
import com.spdev.entity.enums.RoomType;
import com.spdev.util.HibernateTestUtil;
import com.spdev.util.TestDataImporter;
import com.spdev.util.TestEntityUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RoomRepositoryIT {

    private static final Integer EXISTING_HOTEL_ID = 1;
    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    private final Session session = sessionFactory.getCurrentSession();
    private final UserRepository userRepository = new UserRepository(session);
    private final HotelRepository hotelRepository = new HotelRepository(session);
    private final RoomRepository roomRepository = new RoomRepository(session);

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

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var actualRoom = roomRepository.save(validRoom);

        assertThat(actualRoom.getId()).isNotNull();
        assertThat(actualRoom).isEqualTo(validRoom);

        session.getTransaction().rollback();
    }

    @Test
    void checkDelete() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var savedRoom = roomRepository.save(validRoom);

        assertThat(savedRoom.getId()).isNotNull();

        roomRepository.delete(savedRoom);
        var actualRoom = roomRepository.findById(savedRoom.getId());

        assertThat(actualRoom).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdate() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);

        var savedRoom = roomRepository.save(validRoom);
        assertThat(savedRoom.getId()).isNotNull();

        savedRoom.setType(RoomType.TRPL);
        savedRoom.setAvailable(true);
        savedRoom.setAdultBedCount(3);

        roomRepository.update(savedRoom);
        session.clear();

        assertThat(savedRoom.getType()).isEqualTo(RoomType.TRPL);
        assertThat(savedRoom.isAvailable()).isEqualTo(true);
        assertThat(savedRoom.getAdultBedCount()).isEqualTo(3);

        session.getTransaction().rollback();
    }

    @Test
    void findById() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var savedRoom = roomRepository.save(validRoom);

        assertThat(savedRoom.getId()).isNotNull();

        session.clear();
        var actualRoom = roomRepository.findById(savedRoom.getId());

        assertAll(
                () -> assertThat(actualRoom).isPresent(),
                () -> assertThat(actualRoom.get()).isEqualTo(savedRoom)
        );

        session.getTransaction().rollback();
    }

    @Test
    void findAll() {
        session.beginTransaction();

        var actualRooms = roomRepository.findAll();
        assertThat(actualRooms).hasSize(16);

        var roomNos = actualRooms.stream().map(Room::getRoomNo).toList();
        assertThat(roomNos).contains(1, 2, 7, 10, 3, 4, 6, 12, 3, 1, 6, 5, 1, 2, 7, 10);

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllAvailableByHotel() {
        session.beginTransaction();

        var actualRooms = roomRepository.findAllAvailableByHotel("MoscowPlaza");
        assertThat(actualRooms).hasSize(3);

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllAvailableInHotelLocationOrderedAscByPrice() {
        session.beginTransaction();

        var existingHotel = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualRooms = roomRepository.findAllAvailableInHotelLocationOrderedAscByPrice(existingHotel);
        var roomCosts = actualRooms.stream()
                .collect(toMap(Room::getRoomNo, Room::getCost));

        assertThat(roomCosts).containsExactly(
                entry(1, valueOf(1500.99).setScale(2)),
                entry(2, valueOf(2500).setScale(2)),
                entry(7, valueOf(1900.85).setScale(2)));

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllAvailableWithCostFromToOrderedAscByCost() {
        session.beginTransaction();

        var actualRooms = roomRepository.findAllAvailableWithCostFromToOrderedAscByCost(valueOf(1000), valueOf(2000));
        assertThat(actualRooms).hasSize(9);

        var roomCosts = actualRooms.stream().map(Room::getCost).toList();

        assertThat(roomCosts).containsExactly(
                valueOf(1100.85).setScale(2),
                valueOf(1500.00).setScale(2),
                valueOf(1500.99).setScale(2),
                valueOf(1700.00).setScale(2),
                valueOf(1850.58).setScale(2),
                valueOf(1900.00).setScale(2),
                valueOf(1900.85).setScale(2),
                valueOf(1900.85).setScale(2),
                valueOf(1950.85).setScale(2));

        session.getTransaction().rollback();
    }
}