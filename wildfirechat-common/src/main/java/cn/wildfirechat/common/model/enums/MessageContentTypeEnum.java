package cn.wildfirechat.common.model.enums;

/**
 * 消息类型枚举
 */
public enum MessageContentTypeEnum {
    UNKNOWN(0, "未知"),
    TEXT(1, "文本"),
    VOICE(2, "嗓音"),
    IMAGE(3, "图片"),
    LOCATION(4, "地点"),
    FILE(5, "文件"),
    VIDEO(6, "视频"),
    STICKER(7, "贴纸"),
    LINK(8, "关联"),
    P_TEXT(9, "P_TEXT"),
    NAME_CARD(10, "名片"),
    COMPOSITED(11, "复合的"),
    RICH_NOTIFICATION(12, "丰富的通知"),
    ARTICLES(13, "文章"),
    ENTER_CHANNEL_CHAT(71, "进入频道聊天"),
    LEAVE_CHANNEL_CHAT(72, "离开频道聊天"),
    RECALL(80, "记起"),
    DELETE(81, "删除"),
    TIP(90, "提示"),
    TYPING(91, "打字"),
    FRIEND_GREETING(92, "朋友问候"),
    FRIEND_ADDED(93, "朋友已添加"),
    PC_LOGIN_REQUEST(94, "PC登录请求"),
    CREATE_GROUP(104, "创建组"),
    ADD_GROUP_MEMBER(105, "添加组成员"),
    KICKOFF_GROUP_MEMBER(106, "剔除组成员"),
    QUIT_GROUP(107, "退出组"),
    DISMISS_GROUP(108, "解散组"),
    TRANSFER_GROUP_OWNER(109, "转移组所有者"),
    CHANGE_GROUP_NAME(110, "更改组名称"),
    MODIFY_GROUP_ALIAS(111, "修改组别名"),
    CHANGE_GROUP_PORTRAIT(112, "更改群组头像"),
    CHANGE_GROUP_MUTE(113, "更改组静音"),
    CHANGE_GROUP_JOINTYPE(114, "更改组加入类型"),
    CHANGE_GROUP_PRIVATECHAT(115, "更改群组私聊"),
    CHANGE_GROUP_SEARCHABLE(116, "更改组可搜索"),
    SET_GROUP_MANAGER(117, "设置组管理器"),
    MUTE_GROUP_MEMBER(118, "静音组成员"),
    ALLOW_GROUP_MEMBER(119, "允许组成员"),
    KICKOFF_GROUP_MEMBER_VISIBLE_NOTIFICATION(120, "启动组成员可见通知"),
    QUIT_GROUP_VISIBLE_NOTIFICATION(121, "退出组可见通知"),
    MODIFY_GROUP_EXTRA(122, "修改组额外"),
    MODIFY_GROUP_MEMBER_EXTRA(123, "修改组成员额外"),
    SENSITIVE_HIT(1001, "敏感词命中");

    private final int value;

    private final String message;

    MessageContentTypeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static MessageContentTypeEnum parse(Integer value) {
        if (value != null) {
            for (MessageContentTypeEnum info : values()) {
                if (info.value == value) {
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value + "|" + message;
    }
}
