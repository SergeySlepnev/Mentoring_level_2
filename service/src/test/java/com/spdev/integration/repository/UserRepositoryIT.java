package com.spdev.integration.repository;

import com.spdev.dto.UserFilter;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.LinkedHashMap;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private static final Integer EXISTING_USER_ID = 1;
    private static final Integer NO_PREDICATE_PAGE_SIZE = 2;
    private static final Integer PREDICATE_PAGE_SIZE = 1;

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void checkFindAllByFilterWithNoPredicates() {
        var noPredicateFilter = UserFilter.builder().build();
        var noPredicateUsers = userRepository.findAllByFilter(noPredicateFilter, NO_PREDICATE_PAGE_SIZE);

        assertThat(noPredicateUsers.getTotalPages()).isEqualTo(3);
        assertThat(noPredicateUsers).hasSize(5);

        var userRegisterDates = noPredicateUsers.stream()
                .collect(toMap(User::getEmail, user -> user.getRegisteredAt().toLocalDate().toString(), (previousEmail, newEmail) -> newEmail, LinkedHashMap::new));

        assertThat(userRegisterDates).containsExactly(
                entry("FirstOwner@gmail.com", "2022-11-18"),
                entry("SecondOwner@gmail.com", "2022-11-15"),
                entry("SecondUser@gmail.com", "2022-10-21"),
                entry("FirstUser@gmail.com", "2022-10-12"),
                entry("AdminEmail@gmail.com", "2022-09-08"));
    }

    @Test
    void checkFindAllFilteredByRoleStatus() {
        var roleStatusFilter = UserFilter.builder()
                .role(Role.OWNER)
                .status(Status.NEW)
                .build();

        var filteredUsers = userRepository.findAllByFilter(roleStatusFilter, PREDICATE_PAGE_SIZE);

        assertThat(filteredUsers.getTotalPages()).isEqualTo(2);
        assertThat(filteredUsers).hasSize(2);
    }

    @Test
    void checkUpdate() {
        var existingUser = userRepository.findById(EXISTING_USER_ID);
        assertThat(existingUser).isPresent();

        existingUser.ifPresent(user -> {
            user.setEmail("EmailAfterUpdate@gmail.com");
            user.setPassword("PasswordAfterUpdate");
            user.setPhone("0-(000)-00-00-00");
            userRepository.update(user);
        });

        entityManager.clear();
        var updatedUser = userRepository.findById(EXISTING_USER_ID);

        updatedUser.ifPresent(user ->
                assertAll(
                        () -> assertThat(user.getEmail()).isEqualTo("EmailAfterUpdate@gmail.com"),
                        () -> assertThat(user.getPassword()).isEqualTo("PasswordAfterUpdate"),
                        () -> assertThat(user.getPhone()).isEqualTo("0-(000)-00-00-00"))
        );
    }
}