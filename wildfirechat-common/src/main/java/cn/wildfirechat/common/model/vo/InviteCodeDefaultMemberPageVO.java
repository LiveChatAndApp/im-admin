package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * View Object 邀请码预设好友列表页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "邀请码预设好友列表")
public class InviteCodeDefaultMemberPageVO {
    @ApiModelProperty(value = "流水号ID")
    private Long id;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "会员帐号/GID")
    private String account;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "群名称")
    private String groupName;

    @ApiModelProperty(value = "欢迎词")
    private String welcomeText;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
