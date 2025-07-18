package com.challang.backend.review.entity;

import com.challang.backend.liquor.entity.Liquor;
import com.challang.backend.review.dto.request.ReviewUpdateRequestDto;
import com.challang.backend.user.entity.User;
import com.challang.backend.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquor_id", nullable = false)
    private Liquor liquor;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "dislike_count")
    @Builder.Default
    private Integer dislikeCount = 0;

    @Column(name = "report_count")
    @Builder.Default
    private Integer reportCount = 0;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewTag> reviewTags = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewReport> reports = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewReaction> reactions = new ArrayList<>();

    public void update(ReviewUpdateRequestDto request, List<ReviewTag> newTags) {
        if (request.content() != null) {
            this.content = request.content();
        }
        if (request.imageUrl() != null) {
            this.imageUrl = request.imageUrl();
        }
        this.rating = request.rating();

        this.reviewTags.clear();
        this.reviewTags.addAll(newTags);
    }

    public void increaseReactionCount(ReactionType reactionType) {
        if (reactionType == ReactionType.LIKE) {
            if (this.likeCount == null) this.likeCount = 0;
            this.likeCount++;
        } else {
            if (this.dislikeCount == null) this.dislikeCount = 0;
            this.dislikeCount++;
        }
    }

    public void decreaseReactionCount(ReactionType reactionType) {
        if (reactionType == ReactionType.LIKE) {
            if (this.likeCount == null) this.likeCount = 0;
            this.likeCount--;
        } else {
            if (this.dislikeCount == null) this.dislikeCount = 0;
            this.dislikeCount--;
        }
    }

    public void increaseReportCount() {
        if (this.reportCount == null) {
            this.reportCount = 0;
        }
        this.reportCount++;
    }

    public void changeReaction(ReactionType from, ReactionType to) {
        decreaseReactionCount(from);
        increaseReactionCount(to);
    }

    public void addTags(List<ReviewTag> reviewTags) {
        this.reviewTags.addAll(reviewTags);
    }

    @PreRemove
    private void preRemove() {
        if (liquor != null) {
            liquor.handleReviewDeletion(this.rating);
        }
    }
}