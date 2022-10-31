package com.spdev.integration.repository;

import com.spdev.entity.Hotel;
import com.spdev.entity.enums.Status;
import com.spdev.integration.annotation.IT;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestEntityUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
class HotelRepositoryIT {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Test
    void checkSave() {
        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var actualHotel = hotelRepository.save(validHotel);

        assertThat(actualHotel.getId()).isNotNull();
        assertThat(actualHotel).isEqualTo(validHotel);
    }

    @Test
    void checkDelete() {
        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var savedHotel = hotelRepository.save(validHotel);

        assertThat(savedHotel.getId()).isNotNull();

        hotelRepository.delete(savedHotel);
        var actualHotel = hotelRepository.findById(savedHotel.getId());

        assertThat(actualHotel).isEmpty();
    }

    @Test
    void checkUpdate() {
        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);

        var savedHotel = hotelRepository.save(validHotel);
        assertThat(savedHotel.getId()).isNotNull();

        savedHotel.setName("NameAfterUpdate");
        savedHotel.setAvailable(false);
        savedHotel.setStatus(Status.BLOCKED);

        hotelRepository.update(savedHotel);
        entityManager.clear();

        assertThat(savedHotel.getName()).isEqualTo("NameAfterUpdate");
        assertThat(savedHotel.isAvailable()).isEqualTo(false);
        assertThat(savedHotel.getStatus()).isEqualTo(Status.BLOCKED);
    }

    @Test
    void findById() {
        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var savedHotel = hotelRepository.save(validHotel);

        assertThat(savedHotel.getId()).isNotNull();

        entityManager.clear();
        var actualHotel = hotelRepository.findById(savedHotel.getId());

        assertAll(
                () -> assertThat(actualHotel).isPresent(),
                () -> assertThat(actualHotel.get()).isEqualTo(savedHotel)
        );
    }

    @Test
    void findAll() {
        var actualHotels = hotelRepository.findAll();
        assertThat(actualHotels).hasSize(5);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).containsExactly("MoscowPlaza", "MoscowHotel", "KievPlaza", "PiterPlaza", "MinskPlaza");
    }

    @Test
    void checkFindAllAvailableByLocality() {
        entityManager.getEntityGraph("withHotelContent");

        var actualHotels = hotelRepository.findAllAvailableByLocality("Moscow");
        assertThat(actualHotels).hasSize(2);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).contains("MoscowPlaza", "MoscowHotel");
    }

    @Test
    void checkFindByOwnerEmailAndPhone() {
        var actualHotels = hotelRepository.findByOwnerEmailAndPhone("OwnerEmail@gmail.com", "+3-958-98-89-555");
        assertThat(actualHotels).hasSize(3);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).contains("MoscowPlaza", "MoscowHotel", "KievPlaza");
    }

    @Test
    void checkFindAllByStarOrderedByStarThenName() {
        entityManager.getEntityGraph("withHotelContent");

        var actualHotels = hotelRepository.findAllByStarOrderedByStarThenName();
        assertThat(actualHotels).hasSize(5);

        var actualHotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(actualHotelNames).containsExactly("PiterPlaza", "MoscowPlaza", "KievPlaza", "MinskPlaza", "MoscowHotel");
    }

    @Test
    void checkFindAllInLocalityOrderedDescByRating() {
        var actualHotels = hotelRepository.findAllInLocalityOrderedDescByRating("Minsk");
        assertThat(actualHotels).hasSize(1);
    }
}