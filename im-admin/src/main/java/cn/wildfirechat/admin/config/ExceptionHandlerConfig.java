package cn.wildfirechat.admin.config;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.wildfirechat.common.i18n.I18nBase;
import cn.wildfirechat.common.model.vo.ResponseVO;
import cn.wildfirechat.common.support.SpringMessage;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {

	@Resource
	private SpringMessage message;

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseVO<?> illegalArgumentException(IllegalArgumentException e) {
		if (e.getStackTrace().length > 0) {
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			if (!stackTraceElement.getClassName().equals(Assert.class.getName())
					&& !stackTraceElement.getClassName().equals(cn.wildfirechat.common.support.Assert.class.getName())
					&& !stackTraceElement.getClassName().equals(org.springframework.util.Assert.class.getName())) {
				e.printStackTrace();
			} else {
				log.info(e.getMessage());
			}
		}
		return ResponseVO.error(e.getMessage());
	}

	// Valid 错误管理, 关闭则以Exception管理
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseVO<?> validationException(MethodArgumentNotValidException e) {
	 	return ResponseVO.error(e.getMessage());
	 }

	@ExceptionHandler(Exception.class)
	public ResponseVO<?> exceptionHandler(Exception e) {
		e.printStackTrace();
		return ResponseVO.error(message.getMessage(I18nBase.SYSTEM_ERROR));
	}
}
