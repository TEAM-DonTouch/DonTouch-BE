package shop.dontouch.dontouch_be.domain.auth.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.domain.user.constant.LoginPlatform;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class AppleOAuthClient {
  /*public SocialUserInfo getUserInfo(AppleOAuthLoginRequest request) {
    *//*
     * TODO:
     * 1. token = Apple identityToken
     * 2. Apple public key 가져오기
     * 3. JWT 서명 검증
     * 4. iss/aud/exp 검증
     * 5. sub -> providerId
     * 6. email 추출
     *//*

    throw new CustomException(ErrorCode.SOCIAL_LOGIN_FAILED);
  }*/
}