package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Form 最後聊天列表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageQueryForm {
    @ApiModelProperty(value = "接收者ID")
    private Long receiverId;

    @ApiModelProperty(value = "会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道")
    private Integer chatType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建开始时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建结束时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;
}
