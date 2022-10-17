package com.spdev.util;

import com.spdev.entity.BookingRequest;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.entity.HotelDetails;
import com.spdev.entity.PersonalInfo;
import com.spdev.entity.Review;
import com.spdev.entity.ReviewContent;
import com.spdev.entity.Room;
import com.spdev.entity.User;
import com.spdev.entity.enums.ContentType;
import com.spdev.entity.enums.Rating;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.RoomType;
import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static com.spdev.entity.enums.ContentType.PHOTO;
import static com.spdev.entity.enums.ContentType.VIDEO;
import static java.time.LocalDate.of;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var admin = saveUser(session, Role.ADMIN, "AdminEmail@gmail.com", "AdminPassword",
                    "8-835-66-99-333", "Sergey", "Sidorov",
                    LocalDate.of(1985, Month.APRIL, 22), "AdminAvatar.jpg", Status.APPROVED);
            var natalya = saveUser(session, Role.USER, "FirstUser@gmail.com", "FirstUserPassword",
                    "+3-958-98-89-000", "Natalya", "Stepanova",
                    LocalDate.of(2001, Month.OCTOBER, 22),
                    "UserAvatar.jpg", Status.NEW);
            var michail = saveUser(session, Role.USER, "SecondUser@gmail.com", "SecondUserPassword",
                    "+3-958-98-89-654", "Michail", "Malyshev",
                    LocalDate.of(2008, Month.OCTOBER, 22),
                    "UserAvatar.jpg", Status.NEW);
            var andrey = saveUser(session, Role.OWNER, "OwnerEmail@gmail.com", "OwnerPassword",
                    "+3-958-98-89-555", "Andrey", "Petrov",
                    LocalDate.of(2000, Month.MARCH, 15), "UserAvatar.jpg", Status.NEW);
            var jack = saveUser(session, Role.OWNER, "SecondOwner@gmail.com", "FirstOwnerPassword",
                    "+3-958-98-89-666", "Jack", "Ivanov",
                    LocalDate.of(2000, Month.MARCH, 15), "UserAvatar.jpg", Status.NEW);

            var moscowPlaza = saveHotel(session, andrey, "MoscowPlaza", true, Status.APPROVED);
            var moscowHotel = saveHotel(session, andrey, "MoscowHotel", true, Status.APPROVED);
            var kievPlaza = saveHotel(session, andrey, "KievPlaza", true, Status.APPROVED);
            var piterPlaza = saveHotel(session, jack, "PiterPlaza", true, Status.NEW);
            var minskPlaza = saveHotel(session, jack, "MinskPlaza", true, Status.APPROVED);

            saveHotelDetails(session, moscowPlaza, "1111-111-111", "Russia", "Moscow", "West",
                    "First", 15, Star.FOUR, "Very good hotel");
            saveHotelDetails(session, moscowHotel, "0-000-0000-000-00", "Russia", "Moscow", "EastSide",
                    "First", 15, Star.TWO, "Not bad hotel");
            saveHotelDetails(session, kievPlaza, "2222-22-22", "Ukraine", "Kiev", "West",
                    "Second", 20, Star.THREE, "Nice hotel");
            saveHotelDetails(session, piterPlaza, "3333-333-333", "Russia", "Saint Petersburg", "East",
                    "Third", 5, Star.FIVE, "The best hotel ever");
            saveHotelDetails(session, minskPlaza, "4444-44-44", "Belarus", "Minsk", "North",
                    "Fourth", 10, Star.TWO, "Really good hotel!");

            saveHotelContent(session, moscowPlaza, "moscowPlaza1.jpg", PHOTO);
            saveHotelContent(session, moscowPlaza, "moscowPlaza.MP4", VIDEO);
            saveHotelContent(session, minskPlaza, "minskPlaza1.jpg", PHOTO);
            saveHotelContent(session, minskPlaza, "minskPlaza.MP4", VIDEO);
            saveHotelContent(session, kievPlaza, "kievPlaza.jpg", PHOTO);
            saveHotelContent(session, kievPlaza, "kievPlaza.MP4", VIDEO);
            saveHotelContent(session, piterPlaza, "piterPlaza.jpg", PHOTO);
            saveHotelContent(session, piterPlaza, "piterPlaza.MP4", VIDEO);

            var moscowPlazaRoom_1 = saveRoom(session, moscowPlaza, 1, RoomType.TRPL, 25.5, 3, 0, BigDecimal.valueOf(1500.99), true, 1, "Nice room in moscowPlaza!");
            var moscowPlazaRoom_2 = saveRoom(session, moscowPlaza, 2, RoomType.QDPL, 45.0, 4, 2, BigDecimal.valueOf(2500), true, 1, "Nice room in moscowPlaza");
            var moscowPlazaRoom_7 = saveRoom(session, moscowPlaza, 7, RoomType.TWIN, 35.5, 2, 1, BigDecimal.valueOf(1900.85), true, 3, "Nice room in moscowPlaza!");
            var moscowPlazaRoom_10 = saveRoom(session, moscowPlaza, 10, RoomType.SGL, 20.5, 1, 0, BigDecimal.valueOf(850.58), false, 5, "Nice room in moscowPlaza!");
            var kievPlazaRoom_3 = saveRoom(session, kievPlaza, 3, RoomType.TRPL, 55.5, 3, 1, BigDecimal.valueOf(1700), true, 1, "Nice room in kievPlaza!");
            var kievPlazaRoom_4 = saveRoom(session, kievPlaza, 4, RoomType.QDPL, 15.0, 4, 2, BigDecimal.valueOf(2900), true, 1, "Nice room in kievPlaza!");
            var kievPlazaRoom_6 = saveRoom(session, kievPlaza, 6, RoomType.TWIN, 35.5, 2, 0, BigDecimal.valueOf(1950.85), true, 3, "Nice room in kievPlaza!");
            var kievPlazaRoom_12 = saveRoom(session, kievPlaza, 12, RoomType.SGL, 23.5, 1, 0, BigDecimal.valueOf(950.58), false, 5, "Nice room in kievPlaza!");
            var piterPlazaRoom_3 = saveRoom(session, piterPlaza, 3, RoomType.TRPL, 25.5, 3, 2, BigDecimal.valueOf(1500), true, 1, "Nice room in piterPlaza!");
            var piterPlazaRoom_1 = saveRoom(session, piterPlaza, 1, RoomType.QDPL, 45.0, 4, 1, BigDecimal.valueOf(2500), true, 1, "Nice room in piterPlaza!");
            var piterPlazaRoom_6 = saveRoom(session, piterPlaza, 6, RoomType.TWIN, 35.5, 2, 1, BigDecimal.valueOf(1900.85), true, 3, "Nice room in piterPlaza!");
            var piterPlazaRoom_5 = saveRoom(session, piterPlaza, 5, RoomType.SGL, 20.5, 1, 0, BigDecimal.valueOf(850.58), false, 5, "Nice room in piterPlaza!");
            var minskPlazaRoom_1 = saveRoom(session, minskPlaza, 1, RoomType.TRPL, 25.5, 3, 1, BigDecimal.valueOf(1900), true, 1, "Nice room in minskPlaza!");
            var minskPlazaRoom_2 = saveRoom(session, minskPlaza, 2, RoomType.QDPL, 45.0, 4, 2, BigDecimal.valueOf(2100), true, 1, "Nice room in minskPlaza!");
            var minskPlazaRoom_7 = saveRoom(session, minskPlaza, 7, RoomType.TWIN, 35.5, 2, 1, BigDecimal.valueOf(1100.85), true, 3, "Nice room in minskPlaza!");
            var minskPlazaRoom_10 = saveRoom(session, minskPlaza, 10, RoomType.SGL, 20.5, 1, 0, BigDecimal.valueOf(1850.58), false, 5, "Nice room in minskPlaza!");

            saveBookingRequest(session, LocalDateTime.of(2022, 10, 10,12,05), moscowPlaza, moscowPlazaRoom_1, natalya, of(2022, Month.OCTOBER, 10), of(2022, Month.OCTOBER, 15), Status.NEW);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 11,12,05), moscowPlaza, moscowPlazaRoom_7, natalya, of(2022, Month.NOVEMBER, 10), of(2022, Month.NOVEMBER, 15), Status.NEW);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 12,12,05), kievPlaza, kievPlazaRoom_3, natalya, of(2022, Month.OCTOBER, 22), of(2022, Month.OCTOBER, 30), Status.APPROVED);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 13,12,05), piterPlaza, piterPlazaRoom_1, natalya, of(2022, Month.DECEMBER, 10), of(2022, Month.DECEMBER, 15), Status.APPROVED);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 14,12,05), minskPlaza, minskPlazaRoom_10, natalya, of(2023, Month.JANUARY, 10), of(2023, Month.JANUARY, 15), Status.PAID);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 15,12,05), kievPlaza, kievPlazaRoom_4, michail, of(2022, Month.OCTOBER, 10), of(2022, Month.OCTOBER, 15), Status.CANCELED);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 16,12,05), piterPlaza, piterPlazaRoom_6, michail, of(2022, Month.DECEMBER, 10), of(2022, Month.DECEMBER, 15), Status.APPROVED);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 17,12,05), minskPlaza, minskPlazaRoom_2, michail, of(2023, Month.JANUARY, 10), of(2023, Month.JANUARY, 15), Status.PAID);
            saveBookingRequest(session, LocalDateTime.of(2022, 10, 18,12,05), kievPlaza, kievPlazaRoom_12, michail, of(2022, Month.OCTOBER, 10), of(2022, Month.OCTOBER, 15), Status.CANCELED);

            var minskPlazaReview_1 = saveReview(session, minskPlaza, natalya, Instant.now(), Rating.FIVE);
            var minskPlazaReview_2 = saveReview(session, minskPlaza, michail, Instant.now(), Rating.THREE);
            var minskPlazaReview_3 = saveReview(session, minskPlaza, michail, Instant.now(), Rating.FOUR);
            var minskPlazaReview_4 = saveReview(session, minskPlaza, natalya, Instant.now(), Rating.TWO);

            saveReviewContent(session, minskPlazaReview_1, "Photo_1.jpg", PHOTO);
            saveReviewContent(session, minskPlazaReview_2, "Photo_2.jpg", VIDEO);
            saveReviewContent(session, minskPlazaReview_4, "Photo_3.jpg", VIDEO);

            session.getTransaction().commit();
        }
    }

    private User saveUser(Session session,
                          Role role, String email, String password, String phoneNumber, String fistName,
                          String lastName, LocalDate birthDate, String photoLink, Status status) {
        var user = User.builder()
                .role(role)
                .email(email)
                .password(password)
                .phone(phoneNumber)
                .personalInfo(PersonalInfo.builder()
                        .firstname(fistName)
                        .lastname(lastName)
                        .birthDate(birthDate)
                        .build())
                .photoLink(photoLink)
                .status(status)
                .build();
        session.save(user);

        return user;
    }

    private Hotel saveHotel(Session session, User owner, String name, boolean available, Status status) {
        var hotel = Hotel.builder()
                .owner(owner)
                .name(name)
                .available(available)
                .status(status)
                .build();
        session.save(hotel);

        return hotel;
    }

    private HotelDetails saveHotelDetails(Session session, Hotel hotel, String phoneNumber, String country, String locality,
                                          String area, String street, Integer floorCount, Star star, String description) {
        var hotelDetails = HotelDetails.builder()
                .hotel(hotel)
                .phoneNumber(phoneNumber)
                .country(country)
                .locality(locality)
                .area(area)
                .street(street)
                .floorCount(floorCount)
                .star(star)
                .description(description)
                .build();
        session.save(hotelDetails);

        return hotelDetails;
    }

    private HotelContent saveHotelContent(Session session, Hotel hotel, String link, ContentType type) {
        var hotelContent = HotelContent.builder()
                .hotel(hotel)
                .link(link)
                .type(type)
                .build();
        session.save(hotelContent);

        return hotelContent;
    }

    private Room saveRoom(Session session, Hotel hotel, Integer roomNo, RoomType type, Double square,
                          Integer adultBedCount, Integer childrenBedCount, BigDecimal cost, boolean available, Integer floor, String description) {
        var room = Room.builder()
                .hotel(hotel)
                .roomNo(roomNo)
                .type(type)
                .square(square)
                .adultBedCount(adultBedCount)
                .childrenBedCount(childrenBedCount)
                .cost(cost)
                .available(available)
                .floor(floor)
                .description(description)
                .build();
        session.save(room);

        return room;
    }

    private BookingRequest saveBookingRequest(Session session, LocalDateTime createdAt, Hotel hotel, Room room, User user,
                                              LocalDate checkIn, LocalDate checkOut, Status status) {
        var bookingRequest = BookingRequest.builder()
                .createdAt(createdAt)
                .hotel(hotel)
                .room(room)
                .user(user)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .status(status)
                .build();

        session.save(bookingRequest);

        return bookingRequest;
    }

    private Review saveReview(Session session, Hotel hotel, User user, Instant createdAt, Rating rating) {
        var review = Review.builder()
                .hotel(hotel)
                .user(user)
                .createdAt(createdAt)
                .rating(rating)
                .build();
        session.save(review);

        return review;
    }

    private ReviewContent saveReviewContent(Session session, Review review, String link, ContentType type) {
        var reviewContent = ReviewContent.builder()
                .review(review)
                .link(link)
                .type(type)
                .build();
        session.save(reviewContent);

        return reviewContent;
    }
}