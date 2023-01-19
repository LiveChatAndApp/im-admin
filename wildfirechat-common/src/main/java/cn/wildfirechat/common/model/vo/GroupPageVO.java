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
 * View Object 群页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "群列表")
public class GroupPageVO implements Serializable {
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "群GID")
    private String gid;

    @ApiModelProperty(value = "群名称(群信息)")
    private String name;

    @ApiModelProperty(value = "群人数")
    private int memberCount;

    @ApiModelProperty(value = "群头像图片")
    private String groupImage;

    @ApiModelProperty(value = "群主")
    private Long managerId;

    @ApiModelProperty(value = "群主昵称")
    private String managerNickName;

    @ApiModelProperty(value = "群主帐号")
    private String managerMemberName;

    @ApiModelProperty(value = "禁言 0:正常 1:禁言普通成员 2:禁言整个群")
    private Integer muteType;

    @ApiModelProperty(value = "私聊 1: 正常, 2: 禁止")
    private Integer privateChat;

    @ApiModelProperty(value = "状态 1: 正常, 2: 解散")
    private Integer status;

    @ApiModelProperty(value = " 群组类型 1:一般,2:广播")
    private Integer groupType;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;
}
