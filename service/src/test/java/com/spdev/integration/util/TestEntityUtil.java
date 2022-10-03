package com.spdev.integration.util;

import com.spdev.entity.BookingRequest;
import com.spdev.entity.Hotel;
import com.spdev.entity.HotelContent;
import com.spdev.entity.HotelDetails;
import com.spdev.entity.PersonalInfo;
import com.spdev.entity.Review;
import com.spdev.entity.ReviewContent;
import com.spdev.entity.Room;
import com.spdev.entity.RoomContent;
import com.spdev.entity.User;
import com.spdev.entity.enums.Rating;
import com.spdev.entity.enums.Role;
import com.spdev.entity.enums.RoomType;
import com.spdev.entity.enums.Star;
import com.spdev.entity.enums.Status;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestEntityUtil {

    public static User getValidUser() {
        return User.builder()
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
    }

    //this user entity doesn't contain role, email,
    // password, firstname, lastname, phone, birthdate and status (NOT NULL constraints in db)
    public static User getInvalidUser() {
        return User.builder()
                .photoLink("randomPhoto.jpg")
                .build();
    }

    public static Hotel getValidHotel() {
        return Hotel.builder()
                .owner(getValidUser())
                .name("HotelPlaza")
                .available(true)
                .hotelStatus(Status.APPROVED)
                .build();
    }

    //this hotel entity doesn't contain owner, name, locality, area,
    // category, phoneNumber, description, isAvailable, hotelStatus (NOT NULL constraints in db)
    public static Hotel getInvalidHotel() {
        return Hotel.builder()
                .build();
    }

    public static HotelDetails getValidHotelDetails() {
        return HotelDetails.builder()
                .hotel(getValidHotel())
                .phoneNumber("8-658-697-69-36")
                .locality("Locality")
                .area("West")
                .street("firstStreet")
                .numbersOfFloors(3)
                .star(Star.TWO)
                .description("Very good hotel")
                .build();
    }

    //this HotelDetails entity doesn't contain hotel, phoneNumber, locality, area,
    // street, numbersOfFloors, star, description(NOT NULL constraints in db)
    public static HotelDetails getInvalidHotelDetails() {
        return HotelDetails.builder()
                .build();
    }

    public static Room getValidRoom() {
        return Room.builder()
                .hotel(getValidHotel())
                .roomNo(2)
                .type(RoomType.TRPL)
                .square(25.4)
                .numberOfBed(3)
                .cost(BigDecimal.valueOf(2500.25))
                .available(true)
                .floor(2)
                .description("Very good room")
                .build();
    }

    //this rom entity doesn't contain hotel, roomNo,
    // type, square, numberOfBed, cost, isAvailable and description (NOT NULL constraints in db)
    public static Room getInvalidRoom() {
        return Room.builder()
                .build();
    }

    public static BookingRequest validBookingRequest() {
        return BookingRequest.builder()
                .createdAt(LocalDateTime.now())
                .hotel(getValidHotel())
                .room(getValidRoom())
                .user(getValidUser())
                .checkIn(LocalDate.of(2022, 9, 26))
                .checkOut(LocalDate.of(2022, 9, 28))
                .requestStatus(Status.APPROVED)
                .build();
    }

    //this booking request entity doesn't contain hotelId, roomId, userId,
    // checkIn, checkOut, requestStatus(NOT NULL constraints in db)
    public static BookingRequest getInvalidBookingRequest() {
        return BookingRequest.builder()
                .build();
    }

    public static Review getValidReview() {
        return Review.builder()
                .hotel(getValidHotel())
                .date(LocalDateTime.now())
                .rating(Rating.ONE)
                .description("Very bad hotel!!!")
                .build();
    }

    //this review entity doesn't contain hotelId, date, rating, description(NOT NULL constraints in db)
    public static Review getInvalidReview() {
        return Review.builder()
                .build();
    }

    public static HotelContent getValidHotelContent() {
        return HotelContent.builder()
                .hotel(getValidHotel())
                .link("hotelPhoto.jpg")
                .build();
    }

    //this HotelContent doesn't contain hotel and link (NOT NULL constraint in db)
    public static HotelContent getInvalidHotelContent() {
        return HotelContent.builder().build();
    }

    public static RoomContent getValidRoomContent() {
        return RoomContent.builder()
                .room(getValidRoom())
                .link("hotelPhoto.jpg")
                .build();
    }

    //this RoomContent doesn't contain room and link (NOT NULL constraint in db)
    public static RoomContent getInvalidRoomContent() {
        return RoomContent.builder().build();
    }

    public static ReviewContent getValidReviewContent() {
        return ReviewContent.builder()
                .review(getValidReview())
                .link("hotelPhoto.jpg")
                .build();
    }

    //this ReviewContent doesn't contain review and link (NOT NULL constraint in db)
    public static ReviewContent getInvalidReviewContent() {
        return ReviewContent.builder().build();
    }
}
