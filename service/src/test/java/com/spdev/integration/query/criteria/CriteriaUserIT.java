package com.spdev.integration.query.criteria;

import com.spdev.entity.enums.Role;
import com.spdev.integration.util.TestDataImporter;
import com.spdev.query.criteria.CriteriaUser;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CriteriaUserIT {

    private static final SessionFactory SESSION_FACTORY = HibernateTestUtil.buildSessionFactory();

    private final CriteriaUser criteriaUser = CriteriaUser.getInstance();

    @BeforeAll
    public static void initDb() {
        TestDataImporter.importData(SESSION_FACTORY);
    }

    @AfterAll
    public static void finish() {
        SESSION_FACTORY.close();
    }

    @Test
    void checkFindAll() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualUsers = criteriaUser.findAll(session);
            assertThat(actualUsers).hasSize(5);

            var firstNames = actualUsers.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
            assertThat(firstNames).containsExactlyInAnyOrder("Sergey", "Natalya", "Michail", "Andrey", "Jack");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindAllByRole() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualUsers = criteriaUser.findAllByRole(session, Role.USER);
            assertThat(actualUsers).hasSize(2);
            assertThat(actualUsers.get(0).getPersonalInfo().getFirstname()).isEqualTo("Natalya");

            var actualAdmins = criteriaUser.findAllByRole(session, Role.ADMIN);
            assertThat(actualAdmins).hasSize(1);
            assertThat(actualAdmins.get(0).getPersonalInfo().getFirstname()).isEqualTo("Sergey");


            var owners = criteriaUser.findAllByRole(session, Role.OWNER);
            assertThat(owners).hasSize(2);
            assertThat(owners.get(0).getPersonalInfo().getFirstname()).isEqualTo("Andrey");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindByFirstName() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualUsers = criteriaUser.findByFirstName(session, "Sergey");
            assertThat(actualUsers).hasSize(1);
            assertThat(actualUsers.get(0).getPersonalInfo().getFirstname()).isEqualTo("Sergey");

            session.getTransaction().rollback();
        }
    }
}
