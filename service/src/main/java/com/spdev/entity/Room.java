package com.spdev.entity;

import com.spdev.entity.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Room.roomContents",
        attributeNodes = {
                @NamedAttributeNode("roomContents")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"roomNo", "type", "square", "bedCount", "floor"})
@ToString(exclude = {"id", "hotel", "requests", "roomContents"})
@Builder
@Entity
public class Room implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hotel hotel;

    private Integer roomNo;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private Double square;

    private Integer adultBedCount;

    private Integer childrenBedCount;

    private BigDecimal cost;

    private boolean available;

    private Integer floor;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<BookingRequest> requests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomContent> roomContents = new ArrayList<>();

    public void addRequest(BookingRequest request) {
        requests.add(request);
        request.setRoom(this);
    }

    public void addRoomContent(RoomContent roomContent) {
        roomContents.add(roomContent);
        roomContent.setRoom(this);
    }
}