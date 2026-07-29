package shop.dontouch.dontouch_be.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.community.entity.Comment;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

  private UUID commentId;
  private UUID postId;
  private UUID authorId;
  private String authorNickname;
  private String authorProfileImageUrl;
  private String content;
  private LocalDateTime createdAt;

  public static CommentResponse from(Comment comment) {
    return CommentResponse.builder()
        .commentId(comment.getId())
        .postId(comment.getPost().getId())
        .authorId(comment.getUser().getId())
        .authorNickname(comment.getUser().getNickname())
        .authorProfileImageUrl(comment.getUser().getProfileImageUrl())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .build();
  }
}
