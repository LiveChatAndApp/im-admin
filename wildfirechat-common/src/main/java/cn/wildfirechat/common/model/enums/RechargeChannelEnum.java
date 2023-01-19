package cn.wildfirechat.common.model.enums;


/**
 * 充值渠道收款方式枚举
 * 消息类型  1:银行卡,2:微信,3:支付宝
 */
public enum RechargeChannelEnum {

    BANKCARD(1,"银行卡"),

    WECHAT(2,"微信"),

    ALIPAY(3,"支付宝");


    private final int value;

    private final String message;

    RechargeChannelEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static RechargeChannelEnum parse(Integer value) {
        if(value != null) {
            for(RechargeChannelEnum info : values()){
                if(info.value == value){
                    return info;
                }
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "RechargeChannelEnum{" +
                "value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
