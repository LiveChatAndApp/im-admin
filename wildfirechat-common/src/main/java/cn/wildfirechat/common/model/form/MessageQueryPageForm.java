package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * Form 讯息搜寻表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageQueryPageForm {

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "用户帐号/群(GID)")
    private String account1;

    @ApiModelProperty(value = "用户帐号/群(GID)")
    private String account2;

    @ApiModelProperty(value = "会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道")
    private Integer chatType;

    @ApiModelProperty(value = "消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它 ")
    private Integer messageType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建开始时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeGt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建结束时间(yyyy-MM-dd HH:mm:ss)")
    private Date createTimeLe;
}
