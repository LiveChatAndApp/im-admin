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
public class DefaultMemberEditForm {

	@ApiModelProperty(value = "预设好友id")
	private Long id;

	@ApiModelProperty(value = "欢迎词")
	private String welcomeText;
}
