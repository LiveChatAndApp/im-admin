package cn.wildfirechat.common.model.enums;

/**
 * 黑名单 1: 一般, 2: 拉黑
 */
public enum FriendBlackedEnum {

    GENERAL(1, "一般"),
    BLOCK(2, "拉黑");

    private final int value;

    private final String message;

    FriendBlackedEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static FriendBlackedEnum convert(Integer blacked) {
        switch (blacked) {
            case 0:
                return FriendBlackedEnum.GENERAL;
            case 1:
                return FriendBlackedEnum.BLOCK;
            default:
                return null;
        }
    }

    public static Integer convert(FriendBlackedEnum enums) {
        switch (enums) {
            case GENERAL:
                return 0;
            case BLOCK:
                return 1;
            default:
                return null;
        }
    }

    public static FriendBlackedEnum parse(Integer value) {
        if (value != null) {
            for (FriendBlackedEnum info : values()) {
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
