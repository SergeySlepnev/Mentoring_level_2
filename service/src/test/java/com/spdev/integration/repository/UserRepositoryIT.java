package com.spdev.integration.repository;

import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import com.spdev.filter.UserFilter;
import com.spdev.integration.IntegrationTestBase;
import com.spdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private static final Integer NO_PREDICATE_PAGE_SIZE = 2;
    private static final Integer PREDICATE_PAGE_SIZE = 1;

    private final UserRepository userRepository;

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
}