package cn.wildfirechat.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Business Object 讯息查询业务对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagePageQueryBO {

    private Long id; // 流水号

    private String account1; // 发送者

    private String account2; // 接收者

    private Integer senderRole; // 发送者角色 1: 系统管理者, 2: 用户端会员 [MessageSenderRoleEnum]

    private Integer messageType; // 消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它 [MessageTypeEnum]

    private Integer chatType; // 会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道 [MessageChatEnum]

    private String content; // 内容

    private String filePath; // 档案路径

    private Boolean isRevert; // 撤回 0:否 1:是

    private Date createTimeGt; // 创建时间

    private Date createTimeLe; // 创建时间
}
