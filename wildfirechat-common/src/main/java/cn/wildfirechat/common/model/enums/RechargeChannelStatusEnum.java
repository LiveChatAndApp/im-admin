package cn.wildfirechat.common.model.enums;

/**
 *
 * 充值渠道收款状态枚举  0:停用,1:启用"
 */

public enum RechargeChannelStatusEnum {

    CLOSE(0, "停用"),

    OPEN(1, "启用");

    private final int value;

    private final String message;

    RechargeChannelStatusEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }


    public static RechargeChannelStatusEnum parse(Integer value) {
        if (value != null) {
            for (RechargeChannelStatusEnum info : values()) {
                if (info.value == value) {
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RechargeChannelStatusEnum{" +
                "value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
