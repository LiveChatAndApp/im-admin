package cn.wildfirechat.common.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class UserJoinChatroomCreateBO {

    /** 用戶ID */
    private String userId;
    /** 聊天室ID */
    private String chatroomId;
    /** 聊天室所有用戶ID */
    private List<String> memberIds;
    /** 聊天室事件 0:進入聊天室 1:離開聊天室 */
    private Integer type;
    /** 操作事件 */
    private Long operationTime;

    public boolean isJoin() {
        return type != null && type == 0;
    }

    public boolean isLeave() {
        return type != null && type == 1;
    }

}
