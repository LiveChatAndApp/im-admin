package cn.wildfirechat.common.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * 自定义扩展国际化工具类
 */
@Slf4j
public class SpringMessage extends MessageSourceAccessor {

    /**
     * Return the default locale to use if no explicit locale has been given.
     * <p>The default implementation returns the default locale passed into the
     * corresponding constructor, or LocaleContextHolder's locale as fallback.
     * Can be overridden in subclasses.
     *
     * @see LocaleContextHolder#getLocale()
     */
    public Locale getLocale() {
        return getDefaultLocale();
    }

    /**
     * Create a new MessageSourceAccessor, using LocaleContextHolder's locale
     * as default locale.
     *
     * @param messageSource the MessageSource to wrap
     * @see LocaleContextHolder#getLocale()
     */
    public SpringMessage(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Create a new MessageSourceAccessor, using the given default locale.
     *
     * @param messageSource the MessageSource to wrap
     * @param defaultLocale the default locale to use for message access
     */
    public SpringMessage(MessageSource messageSource, Locale defaultLocale) {
        super(messageSource, defaultLocale);
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     *
     * @param code code of the message
     * @return the message
     */
    public String get(String code) {
        try {
            return getMessage(code);
        } catch (NoSuchMessageException e) {
            log.warn("警告：国际化代码未配置：{}", code);
            return code;
        }
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     *
     * @param code   code of the message
     * @param locale Locale in which to do lookup
     * @return the message
     */
    public String get(String code, Locale locale) {
        try {
            return getMessage(code, locale);
        } catch (NoSuchMessageException e) {
            log.warn("警告：国际化代码未配置：{}", code);
            return code;
        }
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     *
     * @param code code of the message
     * @param args arguments for the message, or {@code null} if none
     * @return the message
     */
    public String get(String code, @Nullable Object... args) {
        try {
            return getMessage(code, args, getDefaultLocale());
        } catch (NoSuchMessageException e) {
            log.warn("警告：国际化代码未配置：{}", code);
            return code;
        }
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     *
     * @param code   code of the message
     * @param args   arguments for the message, or {@code null} if none
     * @param locale Locale in which to do lookup
     * @return the message
     */
    public String get(String code, Locale locale, @Nullable Object... args) {
        try {
            return getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("警告：国际化代码未配置：{}", code);
            return code;
        }
    }

}
