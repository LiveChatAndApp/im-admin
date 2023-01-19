package cn.wildfirechat.common.model.enums;

public enum BooleanEnum {

    YES(1, "是"),

    NO(0, "否");

    private final Integer value;
    private final String message;

    BooleanEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }
    public String getMessage() {
        return message;
    }

    public static BooleanEnum parse(Integer value) {
        if (value != null) {
            for (BooleanEnum info : values()) {
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
