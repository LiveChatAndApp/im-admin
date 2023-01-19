package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 会员业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddBO {
	private String memberName; // 帐号

	private String nickName;// 昵称

	private String password;// 密码

	private Integer accountType;// 帐号类型 1:普通帐号, 2:管理号

	private String avatarUrl; // 头像

	private String phone; // 手机号

	private String email; // 邮箱

	private Integer gender; // 性别

	private String channel; // 渠道

	private String memo; // 备注
}
