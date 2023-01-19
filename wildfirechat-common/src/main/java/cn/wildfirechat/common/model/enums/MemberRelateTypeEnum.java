package cn.wildfirechat.common.model.enums;

/**
 * 会员建立关系对象类型
 */
public enum MemberRelateTypeEnum {
    MEMBER(1, "会员"),
    GROUP(2, "群组");

    private final int value;
    private final String message;

    MemberRelateTypeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static MemberRelateTypeEnum parse(Integer value) {
        if (value != null) {
            for (MemberRelateTypeEnum info : values()) {
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
