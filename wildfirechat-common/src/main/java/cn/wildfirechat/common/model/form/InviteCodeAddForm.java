package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Form 邀请码新增表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeAddForm {

	@ApiModelProperty(value = "邀请码", required = true)
	@NotBlank(message = "Parameter [inviteCode] cannot be null")
	private String inviteCode;

	@ApiModelProperty(value = "备注")
	private String memo;

	@ApiModelProperty(value = "预设好友用户帐号", required = true)
	private String username;

	@ApiModelProperty(value = "预设好友欢迎词", required = true)
	private String welcomeText;

	@Min(value = 1, message = "Parameter [friendsDefaultType] cannot be less than 1")
	@Max(value = 2, message = "Parameter [friendsDefaultType] cannot be greater than 2")
	@ApiModelProperty(value = "预设好友模式 1: 所有, 2: 轮询", required = true)
	private Integer friendsDefaultType;

	@Min(value = 0, message = "Parameter [status] cannot be less than 0")
	@Max(value = 1, message = "Parameter [status] cannot be greater than 1")
	@ApiModelProperty(value = "状态 0: 关闭, 1: 开启", required = true)
	private Integer status;
}
