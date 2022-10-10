package com.spdev.integration.query.dsl;

import com.spdev.entity.enums.Role;
import com.spdev.integration.util.TestDataImporter;
import com.spdev.query.dsl.DslUser;
import com.spdev.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DslUserIT {

    private static final SessionFactory SESSION_FACTORY = HibernateTestUtil.buildSessionFactory();

    private final DslUser dslUser = DslUser.getInstance();

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

            var actualUsers = dslUser.findAll(session);
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

            var users = dslUser.findAllByRole(session, Role.USER);
            assertThat(users).hasSize(2);
            assertThat(users.get(0).getPersonalInfo().getFirstname()).isEqualTo("Natalya");

            var actualAdmins = dslUser.findAllByRole(session, Role.ADMIN);
            assertThat(actualAdmins).hasSize(1);
            assertThat(actualAdmins.get(0).getPersonalInfo().getFirstname()).isEqualTo("Sergey");


            var actualOwners = dslUser.findAllByRole(session, Role.OWNER);
            assertThat(actualOwners).hasSize(2);
            assertThat(actualOwners.get(0).getPersonalInfo().getFirstname()).isEqualTo("Andrey");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindByFirstName() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualUsers = dslUser.findByFirstName(session, "Sergey");
            assertThat(actualUsers).hasSize(1);
            assertThat(actualUsers.get(0).getPersonalInfo().getFirstname()).isEqualTo("Sergey");

            session.getTransaction().rollback();
        }
    }

    @Test
    void checkFindOlderAge() {
        try (var session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            var actualUsers = dslUser.findOlderAge(session, -18);
            assertThat(actualUsers).hasSize(4);

            var firstNames = actualUsers.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
            assertThat(firstNames).containsExactlyInAnyOrder("Sergey", "Natalya", "Andrey", "Jack");

            session.getTransaction().rollback();
        }
    }
}