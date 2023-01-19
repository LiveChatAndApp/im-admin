package cn.wildfirechat.common.model.vo;

import cn.wildfirechat.common.exception.ResponseCode;

public class ResponseVO<T> {
    public static <T> ResponseVO<T> success(T data) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(ResponseCode.SUCCESS.getCode());
        vo.setMessage(ResponseCode.SUCCESS.getMessage());
        vo.setData(data);
        return vo;
    }
    
    public static <T> ResponseVO<T> error(ResponseCode code) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(code.getCode());
        vo.setMessage(code.getMessage());
    	return vo;
    }

    public static <T> ResponseVO<T> error(ResponseCode code, String message) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(code.getCode());
        vo.setMessage(message);
    	return vo;
    }

    public static <T> ResponseVO<T> error(String message) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(ResponseCode.UNKNOWN_ERROR);
        vo.setMessage(message);
        return vo;
    }

    public static ResponseVO<Void> success() {
    	return success(null);
    }

    private Long code;

    private String message;

    private T data;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setCode(ResponseCode code) {
        this.code = code.getCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
