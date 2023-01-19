package cn.wildfirechat.common.model.enums;

/**
 * 被邀请人身份验证枚举
 */
public enum GroupInviteAuthEnum {

    NORMAL(0, "不需要同意"),

    COMMON(1, "需要同意");

    private final int value;

    private final String message;

    GroupInviteAuthEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupInviteAuthEnum parse(Integer value) {
        if (value != null) {
            for (GroupInviteAuthEnum info : values()) {
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
