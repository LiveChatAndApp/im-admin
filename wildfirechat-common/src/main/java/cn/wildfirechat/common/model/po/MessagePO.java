package cn.wildfirechat.common.model.po;

import cn.wildfirechat.common.model.enums.MessageContentTypeEnum;
import cn.wildfirechat.common.model.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Persistant Object 讯息持久对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagePO {
    private Long id; // 流水号
    private Long mid; // IM信息流水号
    private Long senderId; // 发送者ID
    private String senderAccountName; // 发送者帐号
    private Integer senderRole; // 发送者角色 1: 系统管理者, 2: 用户端会员 [MessageSenderRoleEnum]
    private Long receiverId; // 接收者ID
    private String receiverAccountName; // 接收者帐号/群组GID
    private Integer messageType; // 消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它 [MessageTypeEnum]
    private Integer chatType; // 会话类型 1: 单聊, 2: 群组, 3: 聊天室, 4: 频道 [MessageChatEnum]
    private String content; // 内容
    private String data;// IM信息
    private String filePath; // 档案路径
    private Boolean isRevert; // 撤回 0:否 1:是
    private Boolean isDeleted; // 删除 0:否 1:是
    private Date createTime; // 创建时间
    private String memo; // 备注

    public static String covertMemo(MessageContentTypeEnum enums, String data) {
        StringBuilder memo = new StringBuilder(enums.getMessage());
        switch (enums) {
            case TIP:
                List<String> strs = Arrays.asList("加入聊天室", "离开了聊天室");
                strs.forEach(str -> {
                    if (data.contains(str)) {
                        memo.append(String.format("【%s】", str));
                    }
                });
                break;
            default:
                break;
        }
        return memo.toString();
    }

    public static MessageTypeEnum getMessageTypeEnum(Integer contentType) {
        MessageContentTypeEnum contentTypeEnum = MessageContentTypeEnum.parse(contentType);

        MessageTypeEnum typeEnum;
        switch (contentTypeEnum) {
            case TEXT:
                typeEnum = MessageTypeEnum.TEXT;
                break;
            case VOICE:
                typeEnum = MessageTypeEnum.AUDIO;
                break;
            case IMAGE:
                typeEnum = MessageTypeEnum.IMAGE;
                break;
            case FILE:
                typeEnum = MessageTypeEnum.FILE;
                break;
            case VIDEO:
                typeEnum = MessageTypeEnum.VIDEO;
                break;
            default:
                typeEnum = MessageTypeEnum.OTHER;
                break;
        }
        return typeEnum;
    }
}
