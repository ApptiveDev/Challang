package com.challang.backend.user.service;

import com.challang.backend.archive.repository.ArchiveRepository;
import com.challang.backend.feedback.repository.LiquorFeedbackRepository;
import com.challang.backend.global.exception.BaseException;
import com.challang.backend.preference.repository.LiquorPreferenceLevelRepository;
import com.challang.backend.preference.repository.LiquorPreferenceTagRepository;
import com.challang.backend.preference.repository.LiquorPreferenceTypeRepository;
import com.challang.backend.review.entity.Review;
import com.challang.backend.review.repository.ReviewReactionRepository;
import com.challang.backend.review.repository.ReviewReportRepository;
import com.challang.backend.review.repository.ReviewRepository;
import com.challang.backend.review.repository.ReviewTagRepository;
import com.challang.backend.user.dto.UserActivityCountsResponse;
import com.challang.backend.user.dto.UserInfoResponse;
import com.challang.backend.user.entity.User;
import com.challang.backend.user.exception.UserErrorCode;
import com.challang.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final ArchiveRepository archiveRepository;
    private final LiquorPreferenceTagRepository liquorPreferenceTagRepository;
    private final LiquorPreferenceTypeRepository liquorPreferenceTypeRepository;
    private final LiquorPreferenceLevelRepository liquorPreferenceLevelRepository;
    private final LiquorFeedbackRepository liquorFeedbackRepository;
    private final ReviewTagRepository reviewTagRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }

    public User getByLoginId(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserActivityCountsResponse getUserActivityCounts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        long likedCurationCount = archiveRepository.countByUser(user);
        long writtenReviewCount = reviewRepository.countByWriter(user);

        return new UserActivityCountsResponse(likedCurationCount, writtenReviewCount);
    }

    @Transactional
    public void deleteUser(Long userId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        // 2. 유저와 직접적으로 연관된 데이터들을 먼저 삭제 (JPQL을 사용한 Bulk Delete)
        reviewReactionRepository.deleteAllByUser(user);
        reviewReportRepository.deleteAllByUser(user);
        archiveRepository.deleteAllByUser(user);
        liquorFeedbackRepository.deleteAllByUser(user);
        liquorPreferenceTagRepository.deleteAllByUser(user);
        liquorPreferenceTypeRepository.deleteAllByUser(user);
        liquorPreferenceLevelRepository.deleteAllByUser(user);

        // 3. 유저가 작성한 리뷰들을 조회
        List<Review> reviewsToDelete = reviewRepository.findByWriter(user);

        // 4. 해당 리뷰들과 연관된 데이터들을 삭제
        for (Review review : reviewsToDelete) {
            reviewTagRepository.deleteByReview(review);
            // 다른 유저가 남긴 반응과 신고 기록도 삭제해야 합니다.
            reviewReactionRepository.deleteAllByReview(review);
            reviewReportRepository.deleteAllByReview(review);
        }

        // 5. 유저가 작성한 리뷰들을 삭제
        reviewRepository.deleteAll(reviewsToDelete);

        // 6. 마지막으로 유저 본인을 삭제합니다.
        userRepository.delete(user);
    }
}