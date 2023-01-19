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
 * Persistant Object 讯息列表页面对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "讯息列表")
public class MessagePageVO {

    @ApiModelProperty(value = "序号")
    private Long id;

    @ApiModelProperty(value = "会话类型")
    private String chatTypeString;

    @ApiModelProperty(value = "消息类型显示文字")
    private String messageTypeString;

    @ApiModelProperty(value = "消息类型")
    private Integer messageType;

    @ApiModelProperty(value = "发送者帐号")
    private String senderAccount;

    @ApiModelProperty(value = "接收者帐号")
    private String receiverAccount;

    @ApiModelProperty(value = "发送者腻称")
    private String senderNickname;

    @ApiModelProperty(value = "接收者腻称")
    private String receiverNickname;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "档案Url")
    private String fileUrl;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "撤回 0:否 1:是")
    private Boolean isRevert;

}
