package cn.wildfirechat.common.model.enums;

/**
 * 群組成员行為枚举
 */
public enum GroupMemberBehaviorEnum {

    SYNC(1, "同步"),

    REMOVE(2, "移除");

    private final int value;

    private final String message;

    GroupMemberBehaviorEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupMemberBehaviorEnum parse(Integer value) {
        if (value != null) {
            for (GroupMemberBehaviorEnum info : values()) {
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
