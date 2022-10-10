package com.spdev.query.criteria;

import com.spdev.entity.PersonalInfo_;
import com.spdev.entity.User;
import com.spdev.entity.User_;
import com.spdev.entity.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaUser {

    private static final CriteriaUser INSTANCE = new CriteriaUser();

    /**
     * Find all users
     */

    public List<User> findAll(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Find all users by {role}
     */

    public List<User> findAllByRole(Session session, Role role) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get(User_.ROLE), role));

        return session.createQuery(criteria).list();
    }

    /**
     * Find user by first name
     */

    public List<User> findByFirstName(Session session, String firstName) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));

        return session.createQuery(criteria).list();
    }

    /**
     * Find all users older than 18 years
     */
//через Criteria у меня не получилось сделать такой запрос

    public static CriteriaUser getInstance() {
        return INSTANCE;
    }
}