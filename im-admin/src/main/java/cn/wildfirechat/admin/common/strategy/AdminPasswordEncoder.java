package cn.wildfirechat.admin.common.strategy;

import cn.wildfirechat.admin.common.consts.ConstDigest;

/**
 * Admin用户密码加密策略
 */
public class AdminPasswordEncoder extends MD5PasswordEncoder {

    public AdminPasswordEncoder() {
        setSalt(ConstDigest.SALT);
    }

}
