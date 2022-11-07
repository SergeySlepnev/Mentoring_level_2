package com.spdev.integration.repository;

import com.spdev.dto.HotelFilter;
import com.spdev.entity.Hotel;
import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.LinkedHashMap;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class HotelRepositoryIT extends IntegrationTestBase {

    private static final Integer EXISTING_HOTEL_ID = 1;
    private static final Integer NO_PREDICATE_PAGE_SIZE = 3;
    private static final Integer PREDICATE_PAGE_SIZE = 1;

    private final EntityManager entityManager;
    private final HotelRepository hotelRepository;

    @Test
    void checkFindAllByFilterWithNoPredicates() {
        var noPredicateFilter = HotelFilter.builder().build();
        var noPredicateHotels = hotelRepository.findAllByFilter(noPredicateFilter, NO_PREDICATE_PAGE_SIZE);

        assertThat(noPredicateHotels.getTotalPages()).isEqualTo(2);
        assertThat(noPredicateHotels).hasSize(5);

        var hotelStars = noPredicateHotels.stream()
                .collect(toMap(Hotel::getName, hotel -> hotel.getHotelDetails().getStar().name(),
                        (previousName, newName) -> newName, LinkedHashMap::new));

        assertThat(hotelStars).containsExactly(
                entry("PiterPlaza", "FIVE"),
                entry("MoscowPlaza", "FOUR"),
                entry("KievPlaza", "THREE"),
                entry("MoscowHotel", "TWO"),
                entry("MinskPlaza", "TWO"));
    }

    @Test
    void checkFindAllFilteredByCountryLocalityStar() {
        var countryLocalityStarFilter = HotelFilter.builder()
                .country("Russia")
                .locality("Moscow")
                .star(Star.FOUR)
                .build();

        var filteredHotels = hotelRepository.findAllByFilter(countryLocalityStarFilter, PREDICATE_PAGE_SIZE);

        assertThat(filteredHotels.getTotalPages()).isEqualTo(1);
        assertThat(filteredHotels).hasSize(1);

        var hotelNames = filteredHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).containsExactly("MoscowPlaza");
    }

    @Test
    void checkUpdate() {
        var existingHotel = hotelRepository.findById(EXISTING_HOTEL_ID);
        assertThat(existingHotel).isPresent();

        existingHotel.ifPresent(hotel -> {
            hotel.setName("AfterUpdateHotelName");
            hotel.setStatus(Status.BLOCKED);
            hotelRepository.update(hotel);
        });

        entityManager.clear();
        var updatedHotel = hotelRepository.findById(EXISTING_HOTEL_ID);

        updatedHotel.ifPresent(hotel ->
                assertAll(
                        () -> assertThat(hotel.getName()).isEqualTo("AfterUpdateHotelName"),
                        () -> assertThat(hotel.getStatus()).isEqualTo(Status.BLOCKED)
                ));
    }
}