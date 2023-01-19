package cn.wildfirechat.common.model.enums;

/**
 * 邀请码状态枚举
 */
public enum InviteCodeStatusEnum {

    CLOSE(0, "停用"),

    OPEN(1, "使用中");

    private final int value;
    private final String message;

    InviteCodeStatusEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static InviteCodeStatusEnum parse(Integer value) {
        if (value != null) {
            for (InviteCodeStatusEnum info : values()) {
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
