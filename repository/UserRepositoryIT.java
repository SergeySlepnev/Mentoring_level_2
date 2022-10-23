package com.spdev.integration.repository;

import com.spdev.configuration.TestApplicationConfiguration;
import com.spdev.entity.User;
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

class UserRepositoryIT {

    private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestApplicationConfiguration.class);
    private static final SessionFactory sessionFactory = context.getBean(SessionFactory.class);

    private final Session session = sessionFactory.getCurrentSession();
    private final UserRepository userRepository = context.getBean(UserRepository.class);

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
        var actualUser = userRepository.save(validUser);

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser).isEqualTo(validUser);

        session.getTransaction().rollback();
    }

    @Test
    void checkDelete() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        userRepository.delete(savedUser);
        var actualUser = userRepository.findById(savedUser.getId());
        assertThat(actualUser).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void checkUpdate() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        savedUser.setEmail("EmailAfterUpdate@gmail.com");
        savedUser.setPassword("PasswordAfterUpdate");
        savedUser.setPhone("0-(000)-00-00-00");

        userRepository.update(savedUser);
        session.clear();

        assertThat(savedUser.getEmail()).isEqualTo("EmailAfterUpdate@gmail.com");
        assertThat(savedUser.getPassword()).isEqualTo("PasswordAfterUpdate");
        assertThat(savedUser.getPhone()).isEqualTo("0-(000)-00-00-00");

        session.getTransaction().rollback();
    }

    @Test
    void findById() {
        session.beginTransaction();

        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        session.clear();

        var actualUser = userRepository.findById(savedUser.getId());
        assertAll(
                () -> assertThat(actualUser).isPresent(),
                () -> assertThat(actualUser.get()).isEqualTo(savedUser)
        );

        session.getTransaction().rollback();
    }

    @Test
    void findAll() {
        session.beginTransaction();

        var actualUsers = userRepository.findAll();
        assertThat(actualUsers).hasSize(5);

        var userPhones = actualUsers.stream().map(User::getPhone).toList();
        assertThat(userPhones).containsExactly("8-835-66-99-333", "+3-958-98-89-000", "+3-958-98-89-654", "+3-958-98-89-555", "+3-958-98-89-666");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllOwners() {
        session.beginTransaction();

        var actualOwners = userRepository.findAllOwners();
        assertThat(actualOwners).hasSize(2);

        var ownerNames = actualOwners.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
        assertThat(ownerNames).containsExactly("Andrey", "Jack");

        session.getTransaction().rollback();
    }

    @Test
    void checkFindAllWithUserRole() {
        session.beginTransaction();

        var actualUsers = userRepository.findAllWithUserRole();
        assertThat(actualUsers).hasSize(2);

        var userNames = actualUsers.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
        assertThat(userNames).containsExactly("Natalya", "Michail");

        session.getTransaction().rollback();
    }
}