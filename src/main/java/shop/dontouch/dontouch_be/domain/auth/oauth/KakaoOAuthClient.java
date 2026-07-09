package shop.dontouch.dontouch_be.domain.auth.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthSignupRequest;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Component
public class KakaoOAuthClient {

  private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
  private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com";

  private final RestClient kakaoAuthClient;
  private final RestClient kakaoApiClient;
  private final String kakaoClientId;
  private final String kakaoClientSecret;
  private final String kakaoRedirectUri;

  public KakaoOAuthClient(
    RestClient.Builder restClientBuilder,
    @Value("${oauth.kakao.client-id}") String kakaoClientId,
    @Value("${oauth.kakao.client-secret:}") String kakaoClientSecret,
    @Value("${oauth.kakao.redirect-uri}") String kakaoRedirectUri
  ) {
    this.kakaoAuthClient = restClientBuilder
      .baseUrl(KAKAO_AUTH_BASE_URL)
      .build();

    this.kakaoApiClient = restClientBuilder
      .baseUrl(KAKAO_API_BASE_URL)
      .build();

    this.kakaoClientId = kakaoClientId;
    this.kakaoClientSecret = kakaoClientSecret;
    this.kakaoRedirectUri = kakaoRedirectUri;
  }

  public SocialUserInfo getUserInfo(KakaoOAuthLoginRequest request) {
    return getUserInfoByCode(request.getCode());
  }

  public SocialUserInfo getUserInfo(KakaoOAuthSignupRequest request) {
    return getUserInfoByCode(request.getCode());
  }

  private SocialUserInfo getUserInfoByCode(String code) {
    if (isBlank(code)) {
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }

    try {
      KakaoTokenResponse tokenResponse = requestToken(code);
      KakaoUserResponse userResponse = requestUserInfo(tokenResponse.accessToken());

      if (userResponse == null || userResponse.id() == null) {
        log.warn("kakao oauth: 사용자 정보 응답 없음");
        throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
      }

      String providerId = String.valueOf(userResponse.id());
      String email = extractEmail(userResponse);
      String nickname = extractNickname(userResponse);
      String profileImageUrl = extractProfileImageUrl(userResponse);

      if (isBlank(email)) {
        log.warn("kakao oauth: 이메일 정보 없음");
        throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
      }

      return new SocialUserInfo(
        providerId,
        email,
        nickname,
        profileImageUrl
      );
    } catch (RestClientException e) {
      log.warn("kakao oauth: 카카오 API 호출 실패", e);
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }
  }

  private KakaoTokenResponse requestToken(String code) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", kakaoClientId);
    formData.add("redirect_uri", kakaoRedirectUri);
    formData.add("code", code);

    if (!isBlank(kakaoClientSecret)) {
      formData.add("client_secret", kakaoClientSecret);
    }

    KakaoTokenResponse response = kakaoAuthClient.post()
      .uri("/oauth/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(formData)
      .retrieve()
      .body(KakaoTokenResponse.class);

    if (response == null || isBlank(response.accessToken())) {
      log.warn("kakao oauth: accessToken 발급 실패");
      throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
    }

    return response;
  }

  private KakaoUserResponse requestUserInfo(String accessToken) {
    return kakaoApiClient.get()
      .uri("/v2/user/me")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
      .retrieve()
      .body(KakaoUserResponse.class);
  }

  private String extractEmail(KakaoUserResponse response) {
    if (response.kakaoAccount() == null) {
      return null;
    }

    return response.kakaoAccount().email();
  }

  private String extractNickname(KakaoUserResponse response) {
    if (response.kakaoAccount() != null
        && response.kakaoAccount().profile() != null
        && !isBlank(response.kakaoAccount().profile().nickname())) {
      return response.kakaoAccount().profile().nickname();
    }

    if (response.properties() != null) {
      return response.properties().nickname();
    }

    return null;
  }

  private String extractProfileImageUrl(KakaoUserResponse response) {
    if (response.kakaoAccount() != null
        && response.kakaoAccount().profile() != null
        && !isBlank(response.kakaoAccount().profile().profileImageUrl())) {
      return response.kakaoAccount().profile().profileImageUrl();
    }

    if (response.properties() != null) {
      return response.properties().profileImage();
    }

    return null;
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record KakaoTokenResponse(
    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    Integer expiresIn,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("refresh_token_expires_in")
    Integer refreshTokenExpiresIn,

    String scope
  ) {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record KakaoUserResponse(
    Long id,
    KakaoProperties properties,

    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount
  ) {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record KakaoProperties(
    String nickname,

    @JsonProperty("profile_image")
    String profileImage
  ) {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record KakaoAccount(
    String email,
    KakaoProfile profile
  ) {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record KakaoProfile(
    String nickname,

    @JsonProperty("profile_image_url")
    String profileImageUrl
  ) {
  }
}