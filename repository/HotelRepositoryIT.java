package com.spdev.integration.repository;

import com.spdev.configuration.TestApplicationConfiguration;
import com.spdev.entity.Hotel;
import com.spdev.entity.enums.Status;
import com.spdev.repository.HotelRepository;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestDataImporter;
import com.spdev.util.TestEntityUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class HotelRepositoryIT {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfiguration.class);
    private static final SessionFactory sessionFactory = context.getBean(SessionFactory.class);

    private final Session session = sessionFactory.getCurrentSession();
    private final UserRepository userRepository = context.getBean(UserRepository.class);
    private final HotelRepository hotelRepository = context.getBean(HotelRepository.class);

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

        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var actualHotel = hotelRepository.save(validHotel);

        assertThat(actualHotel.getId()).isNotNull();
        assertThat(actualHotel).isEqualTo(validHotel);

        session.getTransaction().rollback();
    }

    @Test
    void checkDelete() {
        session.beginTransaction();

        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var savedHotel = hotelRepository.save(validHotel);

        assertThat(savedHotel.getId()).isNotNull();

        hotelRepository.delete(savedHotel);
        var actualHotel = hotelRepository.findById(savedHotel.getId());

        assertThat(actualHotel).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdate() {
        session.beginTransaction();

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
        session.clear();

        assertThat(savedHotel.getName()).isEqualTo("NameAfterUpdate");
        assertThat(savedHotel.isAvailable()).isEqualTo(false);
        assertThat(savedHotel.getStatus()).isEqualTo(Status.BLOCKED);

        session.getTransaction().rollback();
    }

    @Test
    void findById() {
        session.beginTransaction();

        var validHotel = TestEntityUtil.getValidHotel();
        var validUser = TestEntityUtil.getValidUser();

        validHotel.setOwner(validUser);
        userRepository.save(validUser);
        var savedHotel = hotelRepository.save(validHotel);

        assertThat(savedHotel.getId()).isNotNull();

        session.clear();
        var actualHotel = hotelRepository.findById(savedHotel.getId());

        assertAll(
                () -> assertThat(actualHotel).isPresent(),
                () -> assertThat(actualHotel.get()).isEqualTo(savedHotel)
        );

        session.getTransaction().rollback();
    }

    @Test
    void findAll() {
        session.beginTransaction();

        var actualHotels = hotelRepository.findAll();
        assertThat(actualHotels).hasSize(5);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).containsExactly("MoscowPlaza", "MoscowHotel", "KievPlaza", "PiterPlaza", "MinskPlaza");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllAvailableByLocality() {
        session.beginTransaction();
        session.getEntityGraph("withHotelContent");

        var actualHotels = hotelRepository.findAllAvailableByLocality("Moscow");
        assertThat(actualHotels).hasSize(2);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).contains("MoscowPlaza", "MoscowHotel");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindByOwnerEmailAndPhone() {
        session.beginTransaction();

        var actualHotels = hotelRepository.findByOwnerEmailAndPhone("OwnerEmail@gmail.com", "+3-958-98-89-555");
        assertThat(actualHotels).hasSize(3);

        var hotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(hotelNames).contains("MoscowPlaza", "MoscowHotel", "KievPlaza");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllByStarOrderedByStarThenName() {
        session.beginTransaction();
        session.getEntityGraph("withHotelContent");

        var actualHotels = hotelRepository.findAllByStarOrderedByStarThenName();
        assertThat(actualHotels).hasSize(5);

        var actualHotelNames = actualHotels.stream().map(Hotel::getName).toList();
        assertThat(actualHotelNames).containsExactly("PiterPlaza", "MoscowPlaza", "KievPlaza", "MinskPlaza", "MoscowHotel");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllInLocalityOrderedDescByRating() {
        session.beginTransaction();

        var actualHotels = hotelRepository.findAllInLocalityOrderedDescByRating("Minsk");
        assertThat(actualHotels).hasSize(1);

        session.getTransaction().rollback();
    }
}