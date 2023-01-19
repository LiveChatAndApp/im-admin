package cn.wildfirechat.common.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Business Object 查看聊天室在线用户对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomQueryOnlineMemberBo {
    private Long id;//流水号ID
    private String cid;//聊天室ID
    private String nickNameOrMemberName;//用戶昵称/账号
}
