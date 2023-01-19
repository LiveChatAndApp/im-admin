package cn.wildfirechat.common.model.enums;

/**
 * 前台操作日誌类型
 */
public enum AppOperateLogEnum {

    LOGIN(1000L, "用户登录", "/login", true),//前台
    LOGIN_FAIL(1001L, "用户登录失败", "/login/fail", true),//前台
    LOGOUT(1002L, "用户登出", "/logout", true);

    private final Long key;//操作行为代号
    private final String name;//操作行为
    private final String api;
    private final boolean isLog;//判断是否需log记录

    AppOperateLogEnum(Long key, String name, String api, boolean isLog) {
        this.key = key;
        this.name = name;
        this.api = api;
        this.isLog = isLog;
    }

    public Long getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getApi() {
        return api;
    }

    public boolean getIsLog() {
        return isLog;
    }

    public static AppOperateLogEnum parseByKey(Long key) {
        if (key != null) {
            for (AppOperateLogEnum info : values()) {
                if (info.key.equals(key)) {
                    return info;
                }
            }
        }
        return null;
    }

    public static AppOperateLogEnum parseByApi(String api) {
        if (api != null) {
            for (AppOperateLogEnum info : values()) {
                if (info.api.equals(api)) {
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "key: "+ key + "|" +
                "name: "+ name + "|"+
                "api: "+ api + "|" +
                "isLog: "+ isLog;
    }

}
