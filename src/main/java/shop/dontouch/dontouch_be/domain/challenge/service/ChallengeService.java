package shop.dontouch.dontouch_be.domain.challenge.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.challenge.dto.request.ChallengeRequest;
import shop.dontouch.dontouch_be.domain.challenge.dto.response.ChallengeResponse;
import shop.dontouch.dontouch_be.domain.challenge.entity.Challenge;
import shop.dontouch.dontouch_be.domain.challenge.entity.UserChallenge;
import shop.dontouch.dontouch_be.domain.challenge.repository.ChallengeRepository;
import shop.dontouch.dontouch_be.domain.challenge.repository.UserChallengeRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeService {

  private final ChallengeRepository challengeRepository;
  private final UserChallengeRepository userChallengeRepository;
  private final UserRepository userRepository;

  @Transactional
  public ChallengeResponse createChallenge(ChallengeRequest request) {
    Challenge challenge = Challenge.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .targetValue(request.getTargetValue())
        .build();

    Challenge savedChallenge = challengeRepository.save(challenge);
    return ChallengeResponse.of(savedChallenge, null);
  }

  public List<ChallengeResponse> getChallenges(UUID userId) {
    List<Challenge> challenges = challengeRepository.findAll();
    List<UUID> challengeIds = challenges.stream().map(Challenge::getId).toList();

    Map<UUID, Integer> myProgressByChallengeId =
        userChallengeRepository.findAllByUserIdAndChallengeIdIn(userId, challengeIds).stream()
            .collect(Collectors.toMap(
                uc -> uc.getChallenge().getId(),
                UserChallenge::getCurrentProgress
            ));

    return challenges.stream()
        .map(challenge -> ChallengeResponse.of(
            challenge,
            myProgressByChallengeId.get(challenge.getId())
        ))
        .toList();
  }

  public List<ChallengeResponse> getMyChallenges(UUID userId) {
    return userChallengeRepository.findAllByUserIdWithChallenge(userId).stream()
        .map(uc -> ChallengeResponse.of(uc.getChallenge(), uc.getCurrentProgress()))
        .toList();
  }

  @Transactional
  public ChallengeResponse joinChallenge(UUID userId, UUID challengeId) {
    Challenge challenge = challengeRepository.findById(challengeId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHALLENGE_NOT_FOUND));

    return userChallengeRepository.findByUserIdAndChallengeId(userId, challengeId)
        .map(existing -> ChallengeResponse.of(challenge, existing.getCurrentProgress()))
        .orElseGet(() -> join(challenge, userId));
  }

  private ChallengeResponse join(Challenge challenge, UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      UserChallenge userChallenge = userChallengeRepository.saveAndFlush(
          UserChallenge.builder()
              .user(user)
              .challenge(challenge)
              .build()
      );
      challenge.increaseParticipantCount();
      return ChallengeResponse.of(challenge, userChallenge.getCurrentProgress());
    } catch (DataIntegrityViolationException e) {
      log.warn("joinChallenge: 이미 참여 중인 챌린지입니다. challengeId={}, userId={}", challenge.getId(), userId);
      return userChallengeRepository.findByUserIdAndChallengeId(userId, challenge.getId())
          .map(existing -> ChallengeResponse.of(challenge, existing.getCurrentProgress()))
          .orElseThrow(() -> e);
    }
  }
}
