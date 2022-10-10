package com.spdev.query.dsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

import static com.spdev.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DslUser {

    private static final DslUser INSTANCE = new DslUser();

    /**
     * Find all users
     */

    public List<User> findAll(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    /**
     * Find all users by {role}
     */

    public List<User> findAllByRole(Session session, Role role) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.role.eq(role))
                .fetch();
    }

    /**
     * Find user by first name
     */

    public List<User> findByFirstName(Session session, String firstName) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstName))
                .fetch();
    }

    /**
     * Find all users older than {age} years
     */

    //в этот метод в тесте передается возраст -18, другого варианта пока не нашёл.
    public List<User> findOlderAge(Session session, int age) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.birthDate.year().subtract(LocalDate.now().getYear()).loe(age))
                .fetch();
    }

    public static DslUser getInstance() {
        return INSTANCE;
    }
}
