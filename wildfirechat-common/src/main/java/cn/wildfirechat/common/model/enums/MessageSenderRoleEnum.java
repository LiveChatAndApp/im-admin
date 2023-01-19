package cn.wildfirechat.common.model.enums;

/**
 * 编辑者角色 1: 系统管理者, 2: 用户端会员枚举
 */
public enum MessageSenderRoleEnum {

    ADMIN(1, "系统管理者"),

    MEMBER(2, "用户端会员");

    private final int value;

    private final String message;

    MessageSenderRoleEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static MessageSenderRoleEnum parse(Integer value) {
        if (value != null) {
            for (MessageSenderRoleEnum info : values()) {
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
