package com.challang.backend.review.entity;

import com.challang.backend.review.entity.Review;
import com.challang.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "review_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type; // LIKE, DISLIKE

    public void updateType(ReactionType type) {
        this.type = type;
    }

    @Builder
    public ReviewReaction(User user, Review review, ReactionType type) {
        this.user = user;
        this.review = review;
        this.type = type;
    }
}

