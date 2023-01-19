package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object 会员数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("会员列表")
public class MemberVO {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "UID")
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "账号")
    private String memberName;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "渠道")
    private String channel;

    @ApiModelProperty(value = "邀请用户ID")
    private Long inviteMemberId;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "个性签名")
    private String signature;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "登陆状态")
    private Integer loginStatus;

    @ApiModelProperty(value = "最后活跃时间")
    private Date lastActiveTime;

    @ApiModelProperty(value = "登陆错误次数")
    private Integer loginErrorCount;

    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    @ApiModelProperty(value = "注册地区")
    private String registerArea;

    @ApiModelProperty(value = "馀额")
    private String balance;

    @ApiModelProperty(value = "冻结金额")
    private String freeze;

    @ApiModelProperty(value = "登陆启用")
    private Boolean loginEnable;

    @ApiModelProperty(value = "添加好友开关")
    private Boolean addFriendEnable;

    @ApiModelProperty(value = "建群开关")
    private Boolean createGroupEnable;

    @ApiModelProperty(value = "帐号类型 1:普通帐号, 2:管理号")
    private Integer accountType;

    @ApiModelProperty(value = "备注")
    private String memo;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;
}
