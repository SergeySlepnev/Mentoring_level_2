package com.spdev.entity;

import com.spdev.entity.enums.Star;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"owner", "name", "locality"})
@ToString(exclude = {"id", "hotel"})
@Builder
@Entity
public class HotelDetails implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    private String phoneNumber;

    private String country;

    private String locality;

    private String area;

    private String street;

    private Integer floorCount;

    @Enumerated(EnumType.STRING)
    private Star star;

    private String description;

    public void setHotel(Hotel hotel) {
        hotel.setHotelDetails(this);
        this.hotel = hotel;
    }
}