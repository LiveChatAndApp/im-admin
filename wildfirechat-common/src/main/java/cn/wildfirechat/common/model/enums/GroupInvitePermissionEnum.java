package cn.wildfirechat.common.model.enums;

/**
 * 邀请权限枚举
 */
public enum GroupInvitePermissionEnum {
    ALL(0, "所有人"),
    GROUP_MEMBER(1, "群成员"),
    MANAGER(2, "管理员");

    private final int value;

    private final String message;

    GroupInvitePermissionEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupInvitePermissionEnum parse(Integer value) {
        if (value != null) {
            for (GroupInvitePermissionEnum info : values()) {
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
