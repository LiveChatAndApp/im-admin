package cn.wildfirechat.common.model.enums;

/**
 * 消息类型枚举
 * 消息类型 1:文本, 2: 语音, 3: 图片, 4: 文件, 5: 视频, 6: 建群, 7: 群加人, 8: 其它
 */
public enum MessageTypeEnum {

    TEXT(1, "文本"),

    AUDIO(2, "语音"),

    IMAGE(3, "图片"),

    FILE(4, "文件"),

    VIDEO(5, "视频"),

    OTHER(8, "其它");

    private final Integer value;

    private final String message;

    MessageTypeEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static Integer convertContentType(MessageTypeEnum enums) {
        switch (enums) {
            case TEXT:
                return 1;
            case AUDIO:
                return 2;
            case IMAGE:
                return 3;
            case FILE:
                return 5;
            case VIDEO:
                return 6;
            default:
                return null;
        }
    }

    public static Integer convert(MessageTypeEnum enums) {
        switch (enums) {
            case TEXT:
                return 0;
            case AUDIO:
                return 2;
            case IMAGE:
                return 1;
            case FILE:
                return 4;
            case VIDEO:
                return 3;
            default:
                return null;
        }
    }

    public static MessageTypeEnum parse(Integer value) {
        if (value != null) {
            for (MessageTypeEnum info : values()) {
                if (info.value == value) {
                    return info;
                }
            }
        }
        return null;
    }

    public static String getMessageByValue(Integer value){
        if (value == null){
            return null;
        }
        for(MessageTypeEnum v : values()){
            if(v.value == value)
            return v.message;
        }
        return null;
    }

    @Override
    public String toString() {
        return value + "|" + message;
    }
}
