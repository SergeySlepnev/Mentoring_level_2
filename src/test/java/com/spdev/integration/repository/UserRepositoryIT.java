package com.spdev.integration.repository;

import com.spdev.entity.User;
import com.spdev.integration.annotation.IT;
import com.spdev.repository.UserRepository;
import com.spdev.util.TestEntityUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@IT
@RequiredArgsConstructor
class UserRepositoryIT {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Test
    void checkSave() {
        var validUser = TestEntityUtil.getValidUser();
        var actualUser = userRepository.save(validUser);

        assertThat(actualUser.getId()).isNotNull();
        assertThat(actualUser).isEqualTo(validUser);
    }

    @Test
    void checkDelete() {
        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        userRepository.delete(savedUser);
        var actualUser = userRepository.findById(savedUser.getId());
        assertThat(actualUser).isEmpty();
    }

    @Test
    void checkUpdate() {
        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        savedUser.setEmail("EmailAfterUpdate@gmail.com");
        savedUser.setPassword("PasswordAfterUpdate");
        savedUser.setPhone("0-(000)-00-00-00");

        userRepository.update(savedUser);
        entityManager.clear();

        assertThat(savedUser.getEmail()).isEqualTo("EmailAfterUpdate@gmail.com");
        assertThat(savedUser.getPassword()).isEqualTo("PasswordAfterUpdate");
        assertThat(savedUser.getPhone()).isEqualTo("0-(000)-00-00-00");
    }

    @Test
    void findById() {
        var validUser = TestEntityUtil.getValidUser();
        var savedUser = userRepository.save(validUser);
        assertThat(savedUser.getId()).isNotNull();

        entityManager.clear();

        var actualUser = userRepository.findById(savedUser.getId());
        assertAll(
                () -> assertThat(actualUser).isPresent(),
                () -> assertThat(actualUser.get()).isEqualTo(savedUser)
        );
    }

    @Test
    void findAll() {
        var actualUsers = userRepository.findAll();
        assertThat(actualUsers).hasSize(5);

        var userPhones = actualUsers.stream().map(User::getPhone).toList();
        assertThat(userPhones).containsExactly("8-835-66-99-333", "+3-958-98-89-000", "+3-958-98-89-654", "+3-958-98-89-555", "+3-958-98-89-666");
    }

    @Test
    void checkFindAllOwners() {
        var actualOwners = userRepository.findAllOwners();
        assertThat(actualOwners).hasSize(2);

        var ownerNames = actualOwners.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
        assertThat(ownerNames).containsExactly("Andrey", "Jack");
    }

    @Test
    void checkFindAllWithUserRole() {
        var actualUsers = userRepository.findAllWithUserRole();
        assertThat(actualUsers).hasSize(2);

        var userNames = actualUsers.stream().map(user -> user.getPersonalInfo().getFirstname()).toList();
        assertThat(userNames).containsExactly("Natalya", "Michail");
    }
}