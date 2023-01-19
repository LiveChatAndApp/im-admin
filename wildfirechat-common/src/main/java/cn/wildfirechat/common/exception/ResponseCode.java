package cn.wildfirechat.common.exception;

public enum ResponseCode {
    SUCCESS(200L, "执行成功"),
    IM_ERROR(1001L, "IMServer错误"),
    UNKNOWN_ERROR(9999L, "错误异常");

    private Long code;
    private String message;

    ResponseCode(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static ResponseCode fromCode(Long code) {
        if (code == null) {
            return null;
        }

        ResponseCode[] values = ResponseCode.values();
        for (ResponseCode e : values) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }

        return UNKNOWN_ERROR;
    }
}
