package com.spdev.integration.repository;

import com.spdev.entity.Room;
import com.spdev.entity.enums.RoomType;
import com.spdev.integration.annotation.IT;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.RoomRepository;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestEntityUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static java.math.BigDecimal.valueOf;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
class RoomRepositoryIT {

    private static final Integer EXISTING_HOTEL_ID = 1;

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Test
    void checkSave() {
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
    }

    @Test
    void checkDelete() {
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
    }

    @Test
    void checkUpdate() {
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
        entityManager.clear();

        assertThat(savedRoom.getType()).isEqualTo(RoomType.TRPL);
        assertThat(savedRoom.isAvailable()).isEqualTo(true);
        assertThat(savedRoom.getAdultBedCount()).isEqualTo(3);
    }

    @Test
    void findById() {
        var validUser = TestEntityUtil.getValidUser();
        var validHotel = TestEntityUtil.getValidHotel();
        var validRoom = TestEntityUtil.getValidRoom();

        validHotel.setOwner(validUser);
        validRoom.setHotel(validHotel);
        userRepository.save(validUser);
        hotelRepository.save(validHotel);
        var savedRoom = roomRepository.save(validRoom);

        assertThat(savedRoom.getId()).isNotNull();

        entityManager.clear();
        var actualRoom = roomRepository.findById(savedRoom.getId());

        assertAll(
                () -> assertThat(actualRoom).isPresent(),
                () -> assertThat(actualRoom.get()).isEqualTo(savedRoom)
        );
    }

    @Test
    void findAll() {
        var actualRooms = roomRepository.findAll();
        assertThat(actualRooms).hasSize(16);

        var roomNos = actualRooms.stream().map(Room::getRoomNo).toList();
        assertThat(roomNos).contains(1, 2, 7, 10, 3, 4, 6, 12, 3, 1, 6, 5, 1, 2, 7, 10);
    }

    @Test
    void checkFindAllAvailableByHotel() {
        var actualRooms = roomRepository.findAllAvailableByHotel("MoscowPlaza");
        assertThat(actualRooms).hasSize(3);
    }

    @Test
    void checkFindAllAvailableInHotelLocationOrderedAscByPrice() {
        var existingHotel = hotelRepository.findById(EXISTING_HOTEL_ID).get();
        var actualRooms = roomRepository.findAllAvailableInHotelLocationOrderedAscByPrice(existingHotel);
        var roomCosts = actualRooms.stream()
                .collect(toMap(Room::getRoomNo, Room::getCost));

        assertThat(roomCosts).containsExactly(
                entry(1, valueOf(1500.99).setScale(2)),
                entry(2, valueOf(2500).setScale(2)),
                entry(7, valueOf(1900.85).setScale(2)));
    }

    @Test
    void checkFindAllAvailableWithCostFromToOrderedAscByCost() {
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
    }
}