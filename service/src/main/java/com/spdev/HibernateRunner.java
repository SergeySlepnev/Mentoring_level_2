package com.spdev;

import com.spdev.entity.PersonalInfo;
import com.spdev.entity.User;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.Status;
import com.spdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        var user = User.builder()
                .role(Role.ADMIN)
                .email("User@mail.ru")
                .password("123")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Petrov")
                        .birthDate(LocalDate.of(1988, 10, 25))
                        .build())
                .photoLink("newPhoto.jpg")
                .userStatus(Status.APPROVED)
                .build();
//
        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            log.info("Transaction is created {}", transaction);

//            session.save(user);

            session.getTransaction().commit();
        }

    }
}
