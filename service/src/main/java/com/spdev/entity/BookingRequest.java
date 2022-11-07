package com.spdev.entity;

import com.spdev.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "hotel", "room", "user"})
@ToString(exclude = {"id", "hotel", "room", "user"})
@Builder
@Entity
public class BookingRequest implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hotel hotel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Room room;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    private LocalDate checkIn;

    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private Status status;
}