package shop.dontouch.dontouch_be.domain.user.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse {

  private UUID userId;
  private Boolean isFollowing;
}
