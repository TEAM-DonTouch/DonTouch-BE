package shop.dontouch.dontouch_be.domain.community.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostLikeResponse;
import shop.dontouch.dontouch_be.domain.community.entity.Post;
import shop.dontouch.dontouch_be.domain.community.entity.PostLike;
import shop.dontouch.dontouch_be.domain.community.repository.PostLikeRepository;
import shop.dontouch.dontouch_be.domain.community.repository.PostRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public PostLikeResponse toggleLike(UUID userId, UUID postId) {
    Post post = postRepository.findById(postId)
      .orElseThrow(() -> {
        log.warn("toggleLike: 존재하지 않는 postId={}", postId);
        return new CustomException(ErrorCode.POST_NOT_FOUND);
      });

    boolean isLiked;

    int deletedCount = postLikeRepository.deleteByUserIdAndPostId(userId, postId);

    if (deletedCount > 0) {
      postRepository.decreaseLikeCount(postId);
      isLiked = false;
    } else {
      User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("toggleLike: 존재하지 않는 userId={}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

      PostLike postLike = PostLike.builder()
        .user(user)
        .post(post)
        .build();

      try {
        postLikeRepository.saveAndFlush(postLike);
      } catch (DataIntegrityViolationException e) {
        log.warn("toggleLike: 동시 요청으로 이미 등록된 좋아요, userId={}, postId={}", userId, postId);
        throw new CustomException(ErrorCode.POST_LIKE_CONFLICT);
      }

      postRepository.increaseLikeCount(postId);
      isLiked = true;
    }

    Post updatedPost = postRepository.findById(postId)
      .orElseThrow(() -> {
        log.warn("toggleLike: 좋아요 처리 중 삭제된 postId={}", postId);
        return new CustomException(ErrorCode.POST_NOT_FOUND);
      });

    return PostLikeResponse.builder()
      .likeCount(updatedPost.getLikeCount())
      .liked(isLiked)
      .build();
  }
}
