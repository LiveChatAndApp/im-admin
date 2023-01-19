package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Form 群私聊
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPrivateChatForm {
    @ApiModelProperty(value = "群組ID")
    private String groupId;

    @ApiModelProperty(value = "私聊 1: 正常, 2: 禁止")
    private Integer privateChat;
}
