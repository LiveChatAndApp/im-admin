package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business Object 讯息新增业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateBO {

	private Long senderId; // 发送者ID

	private String senderAccountName; // 发送者帐号

	private Integer senderRole; // 发送者角色 1: 系统管理者, 2: 用户端会员 [MessageSenderRoleEnum]

	private Long receiverId; // 接收者ID

	private String receiverAccountName; // 接收者帐号/群组GID

	private Integer messageType; // 消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它 [MessageTypeEnum]

	private Integer chatType; // 会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道 [MessageChatEnum]

	private String content; // 内容

	private String filePath; // 档案路径

}
