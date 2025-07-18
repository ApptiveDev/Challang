package com.challang.backend.liquor.entity;

import com.challang.backend.liquor.dto.request.LiquorUpdateRequest;
import com.challang.backend.tag.entity.LiquorTag;
import com.challang.backend.util.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "liquor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Liquor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private LiquorLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private LiquorType type;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "tasting_note", nullable = false)
    private String tastingNote;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "min_abv", nullable = false)
    private Double minAbv;

    @Column(name = "max_abv", nullable = false)
    private Double maxAbv;

    @OneToMany(mappedBy = "liquor")
    private final List<LiquorTag> liquorTags = new ArrayList<>();

    @Column(name = "average_rating")
    @ColumnDefault("0.0")
    private Double averageRating = 0.0;

    @Column(name = "review_count")
    @ColumnDefault("0")
    private Integer reviewCount = 0;

    public void updateAverageRating(Double newAverageRating, Integer newReviewCount) {
        this.averageRating = newAverageRating;
        this.reviewCount = newReviewCount;
    }

    @Builder
    public Liquor(LiquorLevel level, LiquorType type, String name, String imageUrl, String tastingNote, String origin,
                  Double minAbv, Double maxAbv) {
        this.level = level;
        this.type = type;
        this.name = name;
        this.imageUrl = imageUrl;
        this.tastingNote = tastingNote;
        this.origin = origin;
        this.minAbv = minAbv;
        this.maxAbv = maxAbv;
    }

    public void update(LiquorUpdateRequest request, LiquorLevel level, LiquorType type) {
        if (request.name() != null) {
            this.name = request.name();
        }
        if (request.tastingNote() != null) {
            this.tastingNote = request.tastingNote();
        }
        if (request.origin() != null) {
            this.origin = request.origin();
        }
        if (request.minAbv() != null) {
            this.minAbv = request.minAbv();
        }
        if (request.maxAbv() != null) {
            this.maxAbv = request.maxAbv();
        }
        if (request.imageUrl() != null) {
            this.imageUrl = request.imageUrl();
        }
        if (level != null) {
            this.level = level;
        }
        if (type != null) {
            this.type = type;
        }
    }

    public void handleReviewCreation(double newRating) {
        double totalRating = (this.averageRating * this.reviewCount) + newRating;
        this.reviewCount++;
        this.averageRating = totalRating / this.reviewCount;
    }

    public void handleReviewDeletion(double deletedRating) {
        // 리뷰가 하나뿐이었을 경우, 0으로 초기화하여 0으로 나누는 오류 방지
        if (this.reviewCount <= 1) {
            this.reviewCount = 0;
            this.averageRating = 0.0;
        } else {
            double totalRating = (this.averageRating * this.reviewCount) - deletedRating;
            this.reviewCount--;
            this.averageRating = totalRating / this.reviewCount;
        }
    }
}