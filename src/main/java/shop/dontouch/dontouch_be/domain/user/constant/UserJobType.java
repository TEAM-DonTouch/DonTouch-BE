package shop.dontouch.dontouch_be.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserJobType {
  EMPLOYEE,
  FREELANCER,
  SOLE_PROPRIETOR,
  CORPORATE_OFFICER,
  UNEMPLOYED,
  STUDENT,
  OTHER

  /*
  일반 직장인, 근로소득자
  프리랜서, 인적용역 사업자
  개인사업자
  법인 임원, 법인사업자 대표
  무직
  학생
  기타
  */
}
