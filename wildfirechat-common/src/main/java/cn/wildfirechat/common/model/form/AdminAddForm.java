package cn.wildfirechat.common.model.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminAddForm {

	@NotNull(message = "{username}不能为空")
	@ApiModelProperty(value = "用户名")
	private String username;

	@NotNull(message = "{nickname}不能为空")
	@ApiModelProperty(value = "昵称")
	private String nickname;

	@NotNull(message = "{password}不能为空")
	@ApiModelProperty(value = "密码")
	private String password;

	@NotNull(message = "{roleId}不能为空")
	@ApiModelProperty(value = "角色")
	private Long roleId;

	@ApiModelProperty(value = "电子信箱")
	private String email;

	@ApiModelProperty(value = "手机", required = true)
	@NotNull(message = "{phone}不能为空")
	private String phone;

	@ApiModelProperty(value = "状态", example = "1")
	private Integer status;

	@ApiModelProperty(value = "备注")
	private String memo;

}
