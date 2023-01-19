package cn.wildfirechat.common.model.enums;

/**
 * 群资料修改权限枚举
 */
public enum GroupModifyPermissionEnum {

    MANAGER(1, "管理员"),

    ALL(2, " 所有人");

    private final int value;

    private final String message;

    GroupModifyPermissionEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GroupModifyPermissionEnum parse(Integer value) {
        if (value != null) {
            for (GroupModifyPermissionEnum info : values()) {
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
