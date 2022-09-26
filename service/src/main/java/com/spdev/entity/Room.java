package com.spdev.entity;

import com.spdev.entity.enums.IsAvailable;
import com.spdev.entity.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer hotelId;
    private Integer roomNo;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private Double square;
    private Integer numberOfBed;
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private IsAvailable isAvailable;
    private String description;
}
