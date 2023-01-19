package cn.wildfirechat.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * View Object 群成员页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "群成员列表")
public class GroupMemberPageVO implements Serializable {

    @ApiModelProperty(value = "用户流水号")
    private Long id;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "帐号")
    private String memberName;

    @ApiModelProperty(value = "成员类型 1: 成员, 2: 管理员, 3: 群主")
    private Long memberType;

    @ApiModelProperty(value = "性别 1: 保密, 2: 男, 3: 女")
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "注册时间")
    private Date createTime;
}
