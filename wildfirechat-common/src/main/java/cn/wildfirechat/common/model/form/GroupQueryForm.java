package cn.wildfirechat.common.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * Form 群搜寻表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupQueryForm {

    @ApiModelProperty(value = "GID")
    private String gid;

    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "群主帐号")
    private String managerMemberName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建开始时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建结束时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;

    @ApiModelProperty(value = "禁言 0:正常 1:禁言普通成员 2:禁言整个群")
    private Integer muteType;

    @ApiModelProperty(value = "私聊 0: 禁止, 1: 正常")
    private Integer privateChat;

    @ApiModelProperty(value = "状态 1: 正常, 2: 解散")
    private Integer status;

    @ApiModelProperty(value = "群组类型 1:一般,2:广播")
    private Integer groupType;
}
