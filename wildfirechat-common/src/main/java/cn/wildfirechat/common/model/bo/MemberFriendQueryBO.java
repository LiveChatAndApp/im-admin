package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFriendQueryBO {

	private Long memberId; // 会员流水号

	private String phone; // 手机号

	private String memberName; // 用户帐号

	private String nickName; // 昵称
}
