package cn.wildfirechat.common.model.enums;

/**
 * 成员类型枚举
 */
public enum GroupMemberTypeEnum {

    MEMBER(1, "成员"),

    MANAGER(2, "管理员"),

    OWNER(3, "群主");

    private final int value;

    private final String message;

    GroupMemberTypeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupMemberTypeEnum convert(Integer type) {
        switch (type) {
            case 0:
                return GroupMemberTypeEnum.MEMBER;
            case 1:
                return GroupMemberTypeEnum.MANAGER;
            case 2:
                return GroupMemberTypeEnum.OWNER;
            default:
                return null;
        }
    }

    public static GroupMemberTypeEnum parse(Integer value) {
        if (value != null) {
            for (GroupMemberTypeEnum info : values()) {
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
