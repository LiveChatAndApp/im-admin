package cn.wildfirechat.admin.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户登录异常
 */
public class UserLoginException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    private String message;

    public UserLoginException(String msg, Throwable t) {
        super(msg, t);
        this.message = msg;
    }

    public UserLoginException(String msg) {
        super(msg);
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
