package shop.dontouch.dontouch_be.domain.challenge.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.challenge.entity.Challenge;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

  private UUID challengeId;
  private String title;
  private String description;
  private int targetValue;
  private int participantCount;
  private double progress;
  private Boolean isJoined;

  public static ChallengeResponse of(Challenge challenge, Integer myCurrentProgress) {
    boolean joined = myCurrentProgress != null;
    double progress = 0.0;
    if (joined && challenge.getTargetValue() > 0) {
      progress = Math.min(1.0, myCurrentProgress / (double) challenge.getTargetValue());
    }

    return ChallengeResponse.builder()
        .challengeId(challenge.getId())
        .title(challenge.getTitle())
        .description(challenge.getDescription())
        .targetValue(challenge.getTargetValue())
        .participantCount(challenge.getParticipantCount())
        .progress(progress)
        .isJoined(joined)
        .build();
  }
}
