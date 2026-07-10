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
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    return postLikeRepository.findByUserIdAndPostId(userId, postId)
        .map(postLike -> unlike(post, postLike))
        .orElseGet(() -> like(post, userId));
  }

  private PostLikeResponse unlike(Post post, PostLike postLike) {
    postLikeRepository.delete(postLike);
    post.decreaseLikeCount();
    return PostLikeResponse.builder()
        .likeCount(post.getLikeCount())
        .isLiked(false)
        .build();
  }

  private PostLikeResponse like(Post post, UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      postLikeRepository.saveAndFlush(
          PostLike.builder()
              .user(user)
              .post(post)
              .build()
      );
      post.increaseLikeCount();
      return PostLikeResponse.builder()
          .likeCount(post.getLikeCount())
          .isLiked(true)
          .build();
    } catch (DataIntegrityViolationException e) {
      log.warn("toggleLike: 이미 좋아요한 게시글입니다. postId={}, userId={}", post.getId(), userId);
      return PostLikeResponse.builder()
          .likeCount(post.getLikeCount())
          .isLiked(true)
          .build();
    }
  }
}
