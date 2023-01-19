package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * View Object 预设好友/群页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("预设好友-群列表")
public class DefaultMemberPageVO implements Serializable {
    @ApiModelProperty(value = "流水号ID")
    private Long id;

    @ApiModelProperty(value = "预设类型 好友:1, 群:2")
    private Integer defaultType;

    @ApiModelProperty(value = "会员帐号")
    private String memberName;

    @ApiModelProperty(value = "群组ID")
    private String gid;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "群名称")
    private String groupName;

    @ApiModelProperty(value = "欢迎词")
    private String welcomeText;

    @ApiModelProperty(value = "类型 1:所有新注册用户, 2:使用邀请码注册用户")
    private Integer type;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    private Long memberId;

    @JsonIgnore
    private Long groupId;
}
