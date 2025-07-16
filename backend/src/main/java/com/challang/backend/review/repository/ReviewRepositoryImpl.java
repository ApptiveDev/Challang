package com.challang.backend.review.repository;

import com.challang.backend.review.entity.QReview;
import com.challang.backend.review.entity.Review;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // ❗ AWT가 아닌 Spring의 Pageable로 수정
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> findReviewsByLiquorAndTags(Long liquorId, List<Long> tagIds, Pageable pageable) {
        QReview review = QReview.review;

        // 데이터 조회 쿼리
        List<Review> content = queryFactory
                .selectFrom(review)
                .join(review.writer).fetchJoin()
                .where(
                        liquorIdEq(liquorId),
                        tagsIn(tagIds)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, review))
                .fetch();

        // Count 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(
                        liquorIdEq(liquorId),
                        tagsIn(tagIds)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // BooleanBuilder 대신 BooleanExpression 사용을 권장 (재사용성, Null-safe)
    private BooleanExpression liquorIdEq(Long liquorId) {
        return QReview.review.liquor.id.eq(liquorId);
    }

    private BooleanExpression tagsIn(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }
        return QReview.review.reviewTags.any().tag.id.in(tagIds);
    }

    // Pageable의 Sort 객체를 사용하여 동적 정렬 처리
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QReview review) {
        return pageable.getSort().stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    String property = order.getProperty();
                    PathBuilder<Review> pathBuilder = new PathBuilder<>(review.getType(), review.getMetadata());
                    return new OrderSpecifier(direction, pathBuilder.get(property));
                })
                .toArray(OrderSpecifier[]::new);
    }
}