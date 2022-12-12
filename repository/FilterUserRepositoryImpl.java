package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.User;
import com.spdev.filter.UserFilter;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.role(), user.role::eq)
                .add(filter.firstName(), user.personalInfo.firstname::containsIgnoreCase)
                .add(filter.lastName(), user.personalInfo.lastname::containsIgnoreCase)
                .add(filter.status(), user.status::eq)
                .build();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.registeredAt.desc())
                .fetch();
    }
}