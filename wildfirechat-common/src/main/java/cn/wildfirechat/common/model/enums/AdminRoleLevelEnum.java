package cn.wildfirechat.common.model.enums;

/**
 * 角色层级
 */
public enum AdminRoleLevelEnum {

    SUPER(1, "超级管理员"),
    COMMON(2, "普通管理员");

    private final Integer value;
    private final String name;

    AdminRoleLevelEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    public static AdminRoleLevelEnum parse(Integer value) {
        if (value != null) {
            for (AdminRoleLevelEnum info : values()) {
                if (info.value.equals(value)) {
                    return info;
                }
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "value: "+ value + "|" +
                "name: "+ name;
    }

}
