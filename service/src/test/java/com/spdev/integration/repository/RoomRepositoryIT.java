package com.spdev.integration.repository;

import com.spdev.dto.RoomFilter;
import com.spdev.entity.Room;
import com.spdev.entity.enums.RoomType;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class RoomRepositoryIT extends IntegrationTestBase {

    private static final Integer EXISTING_ROOM_ID = 1;
    private static final Integer NO_PREDICATE_PAGE_SIZE = 5;
    private static final Integer PREDICATE_PAGE_SIZE = 3;
    private static final BigDecimal MIN_ROOM_COST = BigDecimal.valueOf(1900);
    private static final BigDecimal MAX_ROOM_COST = BigDecimal.valueOf(2100);

    private final EntityManager entityManager;
    private final RoomRepository roomRepository;

    @Test
    void checkFindAllByFilterWithNoPredicates() {
        var noPredicateFilter = RoomFilter.builder().build();
        var noPredicateRoom = roomRepository.findAllByFilter(noPredicateFilter, NO_PREDICATE_PAGE_SIZE);

        assertThat(noPredicateRoom.getTotalPages()).isEqualTo(4);
        assertThat(noPredicateRoom).hasSize(16);

        var roomCosts = noPredicateRoom.stream().map(Room::getCost).toList();
        assertThat(roomCosts).containsExactly(
                BigDecimal.valueOf(850.58).setScale(2),
                BigDecimal.valueOf(850.58).setScale(2),
                BigDecimal.valueOf(950.58).setScale(2),
                BigDecimal.valueOf(1100.85).setScale(2),
                BigDecimal.valueOf(1500.00).setScale(2),
                BigDecimal.valueOf(1500.99).setScale(2),
                BigDecimal.valueOf(1700.00).setScale(2),
                BigDecimal.valueOf(1850.58).setScale(2),
                BigDecimal.valueOf(1900.00).setScale(2),
                BigDecimal.valueOf(1900.85).setScale(2),
                BigDecimal.valueOf(1900.85).setScale(2),
                BigDecimal.valueOf(1950.58).setScale(2),
                BigDecimal.valueOf(2100.00).setScale(2),
                BigDecimal.valueOf(2500.00).setScale(2),
                BigDecimal.valueOf(2500.00).setScale(2),
                BigDecimal.valueOf(2900.00).setScale(2));
    }

    @Test
    void checkFindAllFilteredByAvailableCostFromCostTo() {
        var availableCostFromToFilter = RoomFilter.builder()
                .available(true)
                .costFrom(MIN_ROOM_COST)
                .costTo(MAX_ROOM_COST)
                .build();

        var filteredRooms = roomRepository.findAllByFilter(availableCostFromToFilter, PREDICATE_PAGE_SIZE);

        assertThat(filteredRooms.getTotalPages()).isEqualTo(2);
        assertThat(filteredRooms).hasSize(5);
    }

    @Test
    void checkUpdate() {
        var existingRoom = roomRepository.findById(EXISTING_ROOM_ID);
        assertThat(existingRoom).isPresent();

        existingRoom.ifPresent(room -> {
            room.setType(RoomType.QDPL);
            room.setCost(BigDecimal.valueOf(3500));
            room.setAdultBedCount(3);
            roomRepository.update(room);
        });

        entityManager.clear();
        var updatedRoom = roomRepository.findById(EXISTING_ROOM_ID);

        updatedRoom.ifPresent(room ->
                assertAll(
                        () -> assertThat(room.getType()).isEqualTo(RoomType.QDPL),
                        () -> assertThat(room.getCost()).isEqualTo(BigDecimal.valueOf(3500).setScale(2)),
                        () -> assertThat(room.getAdultBedCount()).isEqualTo(3)
                ));
    }
}