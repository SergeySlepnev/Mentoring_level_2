package com.spdev.entity;

import com.spdev.entity.enums.Category;
import com.spdev.entity.enums.IsAvailable;
import com.spdev.entity.enums.Status;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ownerId;
    private String name;
    private String locality;
    private String area;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String phoneNumber;
    private String description;
    @Enumerated(EnumType.STRING)
    private IsAvailable isAvailable;
    @Enumerated(EnumType.STRING)
    private Status hotelStatus;

}
