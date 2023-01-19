package cn.wildfirechat.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordHitDTO {
	/** 流水号 */
    private Long id;
    /** 发送者ID */
    private Long senderId;
    /** 发送者帐号/GID */
    private String sender;
    /** 发送者昵称 */
    private String senderNickName;
    /** 接收者ID */
    private Long receiverId;
    /** 接收者帐号/GID */
    private String receiver;
    /** 接收者昵称 */
    private String receiverNickName;
    /** 会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道 */
    private Integer chatType;
    /** 管理员编号 */
    private String content;
    /** 创建者帐号 */
    private String creator;
    /** 创建时间 */
    private Date createTime;

}
