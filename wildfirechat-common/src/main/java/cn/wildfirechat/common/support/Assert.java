package cn.wildfirechat.common.support;

import java.util.function.Supplier;

public class Assert extends org.springframework.util.Assert {
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean expression, Supplier<String> messageSupplier) {
        if (expression) {
            throw new IllegalArgumentException(messageSupplier != null ? messageSupplier.get() : null);
        }
    }
}
