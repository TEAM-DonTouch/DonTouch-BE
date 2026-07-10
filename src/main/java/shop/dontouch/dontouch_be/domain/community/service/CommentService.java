package shop.dontouch.dontouch_be.domain.community.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.community.dto.request.CommentRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.CommentResponse;
import shop.dontouch.dontouch_be.domain.community.entity.Comment;
import shop.dontouch.dontouch_be.domain.community.entity.Post;
import shop.dontouch.dontouch_be.domain.community.repository.CommentRepository;
import shop.dontouch.dontouch_be.domain.community.repository.PostRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Transactional
  public CommentResponse createComment(UUID userId, UUID postId, CommentRequest request) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Comment comment = Comment.builder()
        .user(user)
        .post(post)
        .content(request.getContent())
        .build();

    Comment savedComment = commentRepository.save(comment);
    post.increaseCommentCount();

    return CommentResponse.from(savedComment);
  }

  public List<CommentResponse> getComments(UUID postId) {
    if (!postRepository.existsById(postId)) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId).stream()
        .map(CommentResponse::from)
        .toList();
  }

  @Transactional
  public void deleteComment(UUID userId, UUID postId, UUID commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getPost().getId().equals(postId)) {
      throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
    }

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.delete();
    comment.getPost().decreaseCommentCount();
  }
}
