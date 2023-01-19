package cn.wildfirechat.common.model.enums;

/**
 * 群主禁言設定枚举
 */
public enum GroupMuteTypeEnum {

    NORMAL(0, "自由发言"),

    COMMON(1, "禁言普通成员"),

    ALL(2, "禁言整个群");

    private final Integer value;

    private final String message;

    GroupMuteTypeEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static GroupMuteTypeEnum convert(Integer mute) {
        switch (mute) {
            case 0:
                return GroupMuteTypeEnum.NORMAL;
            case 1:
               return GroupMuteTypeEnum.ALL;
            default:
                return null;
        }
    }

    public static Integer convert(GroupMuteTypeEnum enums) {
        switch (enums) {
            case NORMAL:
                return 0;
            case ALL:
                return 1;
            default:
                return null;
        }
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupMuteTypeEnum parse(Integer value) {
        if (value != null) {
            for (GroupMuteTypeEnum info : values()) {
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
