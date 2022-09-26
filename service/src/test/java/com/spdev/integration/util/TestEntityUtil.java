package com.spdev.integration.util;

import com.spdev.entity.BookingRequest;
import com.spdev.entity.Hotel;
import com.spdev.entity.PersonalInfo;
import com.spdev.entity.PhotoVideo;
import com.spdev.entity.Review;
import com.spdev.entity.Room;
import com.spdev.entity.User;
import com.spdev.entity.enums.Category;
import com.spdev.entity.enums.IsAvailable;
import com.spdev.entity.enums.Rating;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.RoomType;
import com.spdev.entity.enums.Status;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestEntityUtil {

    public static final User FIRST_VALID_USER = User.builder()
            .role(Role.USER)
            .email("first_user_email@gmail.com")
            .password("encryptedWord")
            .personalInfo(PersonalInfo.builder()
                    .firstname("Ivan")
                    .lastname("Petrov")
                    .birthDate(LocalDate.of(1997, 1, 23))
                    .build())
            .phone("+7(359)-778-29-06")
            .photoLink("randomPhoto.jpg")
            .userStatus(Status.NEW)
            .build();

    public static final User SECOND_VALID_USER = User.builder()
            .role(Role.USER)
            .email("second_user_email@gmail.com")
            .password("encryptedWord")
            .personalInfo(PersonalInfo.builder()
                    .firstname("Ivan")
                    .lastname("Petrov")
                    .birthDate(LocalDate.of(1997, 1, 23))
                    .build())
            .phone("+7(935)-778-29-06")
            .photoLink("randomPhoto.jpg")
            .userStatus(Status.NEW)
            .build();

    //this user entity doesn't contain role, email,
    // password, firstname, lastname, phone, birthdate and status (NOT NULL constraints in db)
    public static User INVALID_USER = User.builder()
            .photoLink("randomPhoto.jpg")
            .build();

    public static final Room FIRST_VALID_ROOM = Room.builder()
            .hotelId(1)
            .roomNo(2)
            .type(RoomType.TRPL)
            .square(25.4)
            .numberOfBed(3)
            .cost(BigDecimal.valueOf(2500.25))
            .isAvailable(IsAvailable.NO)
            .description("Very good room")
            .build();

    public static final Room SECOND_VALID_ROOM = Room.builder()
            .hotelId(1)
            .roomNo(1)
            .type(RoomType.SGL)
            .square(15.4)
            .numberOfBed(1)
            .cost(new BigDecimal(1500))
            .isAvailable(IsAvailable.YES)
            .description("Very good room")
            .build();

    //this rom entity doesn't contain hotelId, roomNo,
    // type, square, numberOfBed, cost, isAvailable and description (NOT NULL constraints in db)
    public static Room INVALID_ROOM = Room.builder()
            .build();

    public static final Hotel FIRST_VALID_HOTEL = Hotel.builder()
            .ownerId(1)
            .name("HotelPlaza")
            .locality("Locality")
            .area("West")
            .category(Category.SECOND)
            .phoneNumber("8-658-697-69-36")
            .description("Very good hotel")
            .isAvailable(IsAvailable.YES)
            .hotelStatus(Status.APPROVED)
            .build();

    public static final Hotel SECOND_VALID_HOTEL = Hotel.builder()
            .ownerId(1)
            .name("HotelPlaza")
            .locality("Locality")
            .area("West")
            .category(Category.SECOND)
            .phoneNumber("8-658-697-69-36")
            .description("Very good hotel")
            .isAvailable(IsAvailable.YES)
            .hotelStatus(Status.APPROVED)
            .build();

    //this hotel entity doesn't contain ownerId, name, locality, area,
    // category, phoneNumber, description, isAvailable, hotelStatus (NOT NULL constraints in db)
    public static Hotel INVALID_HOTEL = Hotel.builder()
            .build();

    public static final BookingRequest FIRST_VALID_BOOKING_REQUEST = BookingRequest.builder()
            .hotelId(1)
            .roomId(1)
            .userId(1)
            .checkIn(LocalDate.of(2022, 9, 26))
            .checkOut(LocalDate.of(2022, 9, 28))
            .requestStatus(Status.APPROVED)
            .build();

    public static final BookingRequest SECOND_VALID_BOOKING_REQUEST = BookingRequest.builder()
            .hotelId(1)
            .roomId(1)
            .userId(1)
            .checkIn(LocalDate.of(2022, 10, 26))
            .checkOut(LocalDate.of(2022, 10, 28))
            .requestStatus(Status.NEW)
            .build();

    //this booking request entity doesn't contain hotelId, roomId, userId,
    // checkIn, checkOut, requestStatus(NOT NULL constraints in db)
    public static BookingRequest INVALID_BOOKING_REQUEST = BookingRequest.builder()
            .build();

    public static final Review FIRST_VALID_REVIEW = Review.builder()
            .hotelId(1)
            .date(LocalDateTime.of(2021, 4, 8, 17, 10))
            .rating(Rating.ONE)
            .description("Very bad hotel!!!")
            .build();

    public static final Review SECOND_VALID_REVIEW = Review.builder()
            .hotelId(1)
            .date(LocalDateTime.now())
            .rating(Rating.ONE)
            .description("Very bad hotel!!!")
            .build();

    //this review entity doesn't contain hotelId, date, rating, description(NOT NULL constraints in db)
    public static Review INVALID_REVIEW = Review.builder()
            .build();

    public static final PhotoVideo FIRST_VALID_PHOTO_VIDEO = PhotoVideo.builder()
            .hotelId(1)
            .roomId(1)
            .reviewId(1)
            .link("PhotoLink.jpg")
            .build();

    public static final PhotoVideo SECOND_VALID_PHOTO_VIDEO = PhotoVideo.builder()
            .hotelId(1)
            .roomId(1)
            .reviewId(1)
            .link("SecondPhotoLink.jpg")
            .build();

    //this photoVideo entity doesn't contain hotelId, roomId, reviewId and link (NOT NULL constraints in db)
    public static PhotoVideo INVALID_PHOTO_VIDEO = PhotoVideo.builder()
            .build();
}


