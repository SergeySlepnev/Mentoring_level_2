package com.spdev.entity;

import com.spdev.entity.enums.Rating;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "description")
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ReviewContent> reviewContents = new ArrayList<>();

    public void addReviewContent(ReviewContent reviewContent) {
        reviewContents.add(reviewContent);
        reviewContent.setReview(this);
    }
}
