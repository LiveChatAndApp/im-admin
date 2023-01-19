package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddFriendForm {

//	@Min(value = 1, message = "Parameter [type] cannot be less than 1")
//	@Max(value = 2, message = "Parameter [type] cannot be greater than 2")
//	@ApiModelProperty(value = "类型 1:个人好友, 2: 群")
//	private Integer type; // [MemberRelateTypeEnum]

	@ApiModelProperty(value = "手机号/用户账号")
	private String member;

	@Min(value = 1, message = "Parameter [type] cannot be less than 1")
	@Max(value = 2, message = "Parameter [type] cannot be greater than 2")
	@ApiModelProperty(value = "验证 1: 需验证, 2:免验证")
	private Integer verify;

	@ApiModelProperty(value = "验证消息")
	private String verifyText;
}
