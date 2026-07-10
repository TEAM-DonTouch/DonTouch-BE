package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.dto.response.FollowResponse;
import shop.dontouch.dontouch_be.domain.user.entity.Follow;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.FollowRepository;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;

  @Transactional
  public FollowResponse toggleFollow(UUID followerId, UUID followingId) {
    if (followerId.equals(followingId)) {
      throw new CustomException(ErrorCode.FOLLOW_SELF_NOT_ALLOWED);
    }

    if (!userRepository.existsById(followingId)) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    return followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
        .map(follow -> unfollow(followingId, follow))
        .orElseGet(() -> follow(followerId, followingId));
  }

  private FollowResponse unfollow(UUID followingId, Follow follow) {
    followRepository.delete(follow);
    return FollowResponse.builder()
        .userId(followingId)
        .isFollowing(false)
        .build();
  }

  private FollowResponse follow(UUID followerId, UUID followingId) {
    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    User following = userRepository.findById(followingId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      followRepository.saveAndFlush(
          Follow.builder()
              .follower(follower)
              .following(following)
              .build()
      );
      return FollowResponse.builder()
          .userId(followingId)
          .isFollowing(true)
          .build();
    } catch (DataIntegrityViolationException e) {
      log.warn("toggleFollow: 이미 팔로우 중입니다. followerId={}, followingId={}", followerId, followingId);
      return FollowResponse.builder()
          .userId(followingId)
          .isFollowing(true)
          .build();
    }
  }
}
