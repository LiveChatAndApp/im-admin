package cn.wildfirechat.common.model.enums;

/**
 * 预设好友模式枚举
 */
public enum InviteCodeDefaultFriendTypeEnum {

    ALL(1, "添加所有预设好友"),

    LOOP(2, "轮询使用");

    private final int value;
    private final String message;

    InviteCodeDefaultFriendTypeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static InviteCodeDefaultFriendTypeEnum parse(Integer value) {
        if (value != null) {
            for (InviteCodeDefaultFriendTypeEnum info : values()) {
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
