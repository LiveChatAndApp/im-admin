package cn.wildfirechat.common.model.enums;

import java.util.Arrays;

/**
 * 充值订单状态
 */
public enum RechargeOrderStatusEnum {
    CREATE(0, "订单成立"),
    PENDING_REVIEW(1, "待审核"),
    COMPLETED(2, "已完成"),
    REJECTED(3, "已拒绝"),
    USER_CANCEL(4, "用户取消"),
    TIMEOUT(5, "订单超时");

    private final int value;
    private final String message;

    RechargeOrderStatusEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static boolean isAudit(Integer value) {
        RechargeOrderStatusEnum statusEnum = parse(value);
        return Arrays.asList(PENDING_REVIEW, TIMEOUT).contains(statusEnum);
    }

    public static RechargeOrderStatusEnum parse(Integer value) {
        if (value != null) {
            for (RechargeOrderStatusEnum info : values()) {
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
