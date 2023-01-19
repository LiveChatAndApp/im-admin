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
 * View Object 邀请码页面传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "邀请码列表")
public class InviteCodePageVO {
    @ApiModelProperty(value = "流水号ID")
    private Long id;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "状态 状态 0: 删除, 1: 使用中, 2:停用")
    private Integer status;

    @ApiModelProperty(value = "后台备注")
    private String memo;

    @ApiModelProperty(value = "注册人数")
    private Integer registerCount;

    @ApiModelProperty(value = "预设好友")
    private Integer friendCount;

    @ApiModelProperty(value = "预设群")
    private Integer groupCount;

    @ApiModelProperty(value = "预设好友模式 1: 所有, 2: 轮询")
    private Integer friendsDefaultType;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "最后注册时间")
    private Date registerTime;

}
