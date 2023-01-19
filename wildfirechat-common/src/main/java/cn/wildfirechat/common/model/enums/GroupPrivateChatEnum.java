package cn.wildfirechat.common.model.enums;

/**
 * 私聊 1: 允許私聊, 0: 禁止私聊
 */
public enum GroupPrivateChatEnum {

    NORMAL(1, "允許私聊"),
    BLOCK(0, " 禁止私聊");

    private final int value;

    private final String message;

    GroupPrivateChatEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupPrivateChatEnum convert(Integer privateChat) {
        switch (privateChat) {
            case 0:
                return GroupPrivateChatEnum.NORMAL;
            case 1:
                return GroupPrivateChatEnum.BLOCK;
            default:
                return null;
        }
    }

    public static Integer convert(GroupPrivateChatEnum enums) {
        switch (enums) {
            case NORMAL:
                return 0;
            case BLOCK:
                return 1;
            default:
                return null;
        }
    }

    public static GroupPrivateChatEnum parse(Integer value) {
        if (value != null) {
            for (GroupPrivateChatEnum info : values()) {
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
