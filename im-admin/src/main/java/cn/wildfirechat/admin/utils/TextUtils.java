package cn.wildfirechat.admin.utils;

public final class TextUtils {

    public static String getEnableString(Boolean enable) {
        if (enable == null) {
            return "";
        }
        return Boolean.TRUE.equals(enable) ? "允许" : "禁止";
    }
}
