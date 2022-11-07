package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.dto.UserFilter;
import com.spdev.entity.User;
import com.spdev.querydsl.QPredicates;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static com.spdev.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private static final Integer DEFAULT_PAGE_INDEX = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 2;

    private final EntityManager entityManager;

    @Override
    public Page<User> findAllByFilter(UserFilter filter, Integer pageSize) {
        var predicate = QPredicates.builder()
                .add(filter.role(), user.role::eq)
                .add(filter.firstName(), user.personalInfo.firstname::containsIgnoreCase)
                .add(filter.lastName(), user.personalInfo.lastname::containsIgnoreCase)
                .add(filter.status(), user.status::eq)
                .build();

        var result = new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.registeredAt.desc())
                .fetch();

        var request = buildPageRequest(pageSize);
        return new PageImpl<>(result, request, result.size());
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
        entityManager.flush();
    }

    private PageRequest buildPageRequest(Integer pageSize) {
        return pageSize != null
                ? PageRequest.of(DEFAULT_PAGE_INDEX, pageSize)
                : PageRequest.of(DEFAULT_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }
}