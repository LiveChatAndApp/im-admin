package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "UID")
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "帐号")
    private String memberName;

    @ApiModelProperty(value = "手机")
    private String phone;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    @ApiModelProperty(value = "登陆启用")
    private Boolean loginEnable;

    @ApiModelProperty(value = "添加好友开关")
    private Boolean addFriendEnable;

    @ApiModelProperty(value = "帐号类型 1:普通帐号, 2:管理号")
    @Min(value = 1, message = "Parameter [accountType] cannot be less than 1")
    @Max(value = 2, message = "Parameter [accountType] cannot be greater than 2")
    private Integer accountType;

    @ApiModelProperty(value = "建群开关")
    private Boolean createGroupEnable;

    @ApiModelProperty(value = "管理号开关")
    private Boolean adminEnable;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注册开始时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "注册结束时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;
}
