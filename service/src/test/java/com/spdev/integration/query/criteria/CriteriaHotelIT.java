package com.spdev.integration.query.criteria;

import com.spdev.entity.Hotel;
import com.spdev.entity.enums.Star;
import com.spdev.integration.util.TestDataImporter;
import com.spdev.query.criteria.CriteriaHotel;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CriteriaHotelIT {

    private static final SessionFactory SESSION_FACTORY = HibernateTestUtil.buildSessionFactory();

    private final CriteriaHotel criteriaHotel = CriteriaHotel.getInstance();

    @BeforeAll
    static void setUp() {
        TestDataImporter.importData(SESSION_FACTORY);
    }

    @AfterAll
    static void finish() {
        SESSION_FACTORY.close();
    }

    @Test
    void checkFindAll() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.getEntityGraph("withHotelContent");

            var actualHotels = criteriaHotel.findAll(session);
            assertThat(actualHotels).hasSize(5);

            var actualHotelNames = actualHotels.stream().map(Hotel::getName).toList();
            assertThat(actualHotelNames).contains("MoscowPlaza", "KievPlaza", "PiterPlaza", "MinskPlaza", "MoscowHotel");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindAllByStar() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.getEntityGraph("withHotelContent");

            var actualHotels = criteriaHotel.findAllByStar(session, Star.TWO);
            assertThat(actualHotels).hasSize(2);

            var actualHotelNames = actualHotels.stream().map(Hotel::getName).toList();
            assertThat(actualHotelNames).contains("MoscowHotel", "MinskPlaza");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindAllByLocalityAndStar() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.getEntityGraph("withHotelContent");

            var results = criteriaHotel.findAllByLocalityAndStar(session, "Moscow", Star.FOUR);
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getHotelDetails().getPhoneNumber()).isEqualTo("1111-111-111");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindAllByStarOrderedByStarThenName() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.getEntityGraph("withHotelContent");

            var actualHotels = criteriaHotel.findAllByStarOrderedByStarThenName(session);
            assertThat(actualHotels).hasSize(5);

            var actualHotelNames = actualHotels.stream().map(Hotel::getName).toList();
            assertThat(actualHotelNames).containsExactly("PiterPlaza", "MoscowPlaza", "KievPlaza", "MinskPlaza", "MoscowHotel");

            session.getTransaction().rollback();
        }
    }
}