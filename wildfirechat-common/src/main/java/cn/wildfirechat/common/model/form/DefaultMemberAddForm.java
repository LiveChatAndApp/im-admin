package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 预设好友/群编辑表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMemberAddForm {

	@ApiModelProperty(value = "类型 1:所有新注册用户, 2:使用邀请码注册用户")
	private Integer type;

	@ApiModelProperty(value = "预设类型 好友:1, 群:2")
	private Integer defaultType;

	@ApiModelProperty(value = "预设好友用戶帳號/GID")
	private String username;

	@ApiModelProperty(value = "欢迎词")
	private String welcomeText;

	@ApiModelProperty(value = "邀请码")
	private String inviteCode;
}
