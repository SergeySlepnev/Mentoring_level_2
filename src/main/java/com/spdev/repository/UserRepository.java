package com.spdev.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.spdev.entity.QUser.user;

@Repository
public class UserRepository extends RepositoryBase<Integer, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public List<User> findAllOwners() {
        return new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .where(user.role.eq(Role.OWNER))
                .fetch();
    }

    public List<User> findAllWithUserRole() {
        return new JPAQuery<User>(getEntityManager())
                .select(user)
                .from(user)
                .where(user.role.eq(Role.USER))
                .fetch();
    }
}