package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * View Object 会員聊天列表传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("会員聊天列表")
public class MemberFriendVO {
	@ApiModelProperty(value = "好友流水号")
	private Long id;
	@ApiModelProperty(value = "头像")
	private String avatarUrl;
	@ApiModelProperty(value = "用户流水号")
	private Long memberId;
	@ApiModelProperty(value = "手机号")
	private String phone;
	@ApiModelProperty(value = "用户昵称")
	private String nickName;
	@ApiModelProperty(value = "用户账号")
	private String memberName;
	@ApiModelProperty(value = "用户性别")
	private Integer gender;
	@ApiModelProperty(value = "黑名单 1:一般,2：拉黑")
	private Integer blacked;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@ApiModelProperty(value = "添加好友時間")
	private Date addTime;
}
