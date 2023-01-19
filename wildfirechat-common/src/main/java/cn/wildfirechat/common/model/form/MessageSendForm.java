package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Form 發送群聊天信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendForm {
    @ApiModelProperty(value = "群ID")
    private String gid;

    @ApiModelProperty(value = "文本")
    private String text;

    @ApiModelProperty(value = "消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它")
    private Integer messageType;

    @ApiModelProperty(value = "上传档案")
    private MultipartFile uploadFile;
}
