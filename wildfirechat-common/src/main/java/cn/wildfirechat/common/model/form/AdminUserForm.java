package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserForm {

	@ApiModelProperty(value = "后台用户ID")
	private Long id;

	@ApiModelProperty(value = "后台用户名")
	private String username;

	@ApiModelProperty(value = "聊天帐号")
	private String chatUserName;

	@ApiModelProperty(value = "状态")
	private Integer status;
}
