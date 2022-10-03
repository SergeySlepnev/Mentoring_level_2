package com.spdev.entity;

import com.spdev.entity.enums.RoomType;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "roomNo")
@Builder
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    private Hotel hotel;

    @Column(nullable = false)
    private Integer roomNo;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private Double square;

    private Integer numberOfBed;

    private BigDecimal cost;

    private Boolean available;

    private Integer floor;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<BookingRequest> requests = new ArrayList<>();

    public void addRequest(BookingRequest request) {
        requests.add(request);
        request.setRoom(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RoomContent> roomContents = new ArrayList<>();

    public void addRoomContent(RoomContent roomContent) {
        roomContents.add(roomContent);
        roomContent.setRoom(this);
    }
}