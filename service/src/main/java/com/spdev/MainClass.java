package com.spdev;

import com.querydsl.jpa.impl.JPAQuery;
import com.spdev.entity.User;
import com.spdev.util.HibernateUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.spdev.entity.QUser.user;

public class MainClass {

    public static void main(String[] args) {

        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var fetch = new JPAQuery<User>(session)
                    .select(user.personalInfo.birthDate)
                    .from(user)
                    .where(user.personalInfo.birthDate.year().subtract(LocalDate.now().getYear()).loe(-18))
                    .fetch();

            System.out.println(LocalDate.now().getYear());

            for (LocalDate integer : fetch) {
                System.out.println(integer);

            }

            session.getTransaction().commit();
        }
    }


    public static long findAge(LocalDate birtDate) {

        return ChronoUnit.YEARS.between(LocalDate.now(), birtDate);

    }
}