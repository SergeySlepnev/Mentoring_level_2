package com.spdev.entity;

import com.spdev.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"owner", "name"})
@Builder
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    //Стоит ли в таблице hotel заменить поле owner_id на user_id, чтобы не использовать @JoinColumn?
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status hotelStatus;

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookingRequest> requests = new ArrayList<>();

    public void addRequest(BookingRequest request) {
        requests.add(request);
        request.setHotel(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    private void addReview(Review review) {
        reviews.add(review);
        review.setHotel(this);
    }

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelContent> hotelContents = new ArrayList<>();

    public void addHotelContent(HotelContent hotelContent) {
        hotelContents.add(hotelContent);
        hotelContent.setHotel(this);
    }

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private HotelDetails hotelDetails;
}