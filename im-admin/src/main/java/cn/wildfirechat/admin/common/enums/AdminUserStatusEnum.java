package cn.wildfirechat.admin.common.enums;

/**
 * 管理员状态
 */
public enum AdminUserStatusEnum {

    DISABLE(0, "禁用"),

    NORMAL(1, "正常");

//    FROZEN(2, "冻结")
//
//    CANCEL(3, "注销")

    private final int value;
    private final String message;

    AdminUserStatusEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static AdminUserStatusEnum parse(Integer value) {
        if (value != null) {
            for (AdminUserStatusEnum info : values()) {
                if (info.value == value) {
                    return info;
                }
            }
        }
        return null;
    }

}
