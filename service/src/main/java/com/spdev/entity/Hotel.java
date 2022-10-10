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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withHotelContent",
        attributeNodes = {
                @NamedAttributeNode("hotelContents")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"owner", "name"})
@ToString(exclude = {"rooms", "requests", "reviews", "hotelContents", "hotelDetails"})
@Builder
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<BookingRequest> requests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelContent> hotelContents = new ArrayList<>();

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private HotelDetails hotelDetails;

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void addRequest(BookingRequest request) {
        requests.add(request);
        request.setHotel(this);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setHotel(this);
    }

    public void addHotelContent(HotelContent hotelContent) {
        hotelContents.add(hotelContent);
        hotelContent.setHotel(this);
    }
}