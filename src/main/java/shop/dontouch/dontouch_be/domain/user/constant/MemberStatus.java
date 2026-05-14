package shop.dontouch.dontouch_be.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
  ACTIVE,
  DORMANT,
  SUSPENDED,
  WITHDRAWN
}
