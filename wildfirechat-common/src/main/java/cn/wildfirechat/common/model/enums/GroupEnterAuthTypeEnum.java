package cn.wildfirechat.common.model.enums;

/**
 * 入群验证类型枚举
 */
public enum GroupEnterAuthTypeEnum {

    UNNECESSARY(1, "无需验证"),

    MANAGER(2, "管理员验证"),

    DENIED(3, "不允许入群验证");

    private final int value;

    private final String message;

    GroupEnterAuthTypeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupEnterAuthTypeEnum parse(Integer value) {
        if (value != null) {
            for (GroupEnterAuthTypeEnum info : values()) {
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
