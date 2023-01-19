package cn.wildfirechat.common.model.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Form 查看聊天室在线用户表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomQueryOnlineMemberForm {

    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "聊天室ID", required = true)
    private String cid;

    @ApiModelProperty(value = "用戶昵称/账号")
    private String nickNameOrMemberName;

}
