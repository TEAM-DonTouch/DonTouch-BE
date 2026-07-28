package shop.dontouch.dontouch_be.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeResponse {

  private int likeCount;
  private boolean liked;
}
