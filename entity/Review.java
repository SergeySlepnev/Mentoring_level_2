package com.spdev.entity;

import com.spdev.entity.enums.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "Review.reviewContents",
        attributeNodes = {
                @NamedAttributeNode("reviewContents")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"hotel", "user", "date"})
@ToString(exclude = {"id", "reviewContents"})
@Builder
@Entity
public class Review implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Hotel hotel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    private Instant createdAt;

    private Rating rating;

    private String description;

    @Builder.Default
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewContent> reviewContents = new ArrayList<>();

    public void addReviewContent(ReviewContent reviewContent) {
        reviewContents.add(reviewContent);
        reviewContent.setReview(this);
    }
}