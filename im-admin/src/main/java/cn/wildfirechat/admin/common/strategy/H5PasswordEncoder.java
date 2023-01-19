package cn.wildfirechat.admin.common.strategy;

import cn.wildfirechat.admin.common.consts.ConstDigest;

/**
 * H5密码加密策略
 */
public class H5PasswordEncoder extends MD5PasswordEncoder {

    /**
     * 加密密码
     */
    public String encode(CharSequence rawPassword, String salt, int iterations) {
        setSalt(salt);
        setIterations(iterations);
        return encode(rawPassword);
    }

    /**
     * 加密资金密码
     */
    public String encodePw(CharSequence rawPassword) {
        setSalt(ConstDigest.SALT);
        setIterations(1);
        return encode(rawPassword);
    }

    /**
     * 校验密码
     */
    public boolean matches(CharSequence rawPassword, String encodedPassword, String salt, int iterations) {
        setSalt(salt);
        setIterations(iterations);
        return matches(rawPassword, encodedPassword);
    }

    /**
     * 校验资金密码
     */
    public boolean matchesPw(CharSequence rawPassword, String encodedPassword) {
        setSalt(ConstDigest.SALT);
        setIterations(1);
        return matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        // 明文：abc123
        // 盐：kk5pbdvs
        // 次数：2
        // 密文：992b8863045a09a93cd4692d7ccecbfb

        String pass = "123qwe";
        String salt = "SX&SLC`";
        int iterations = 1;

        System.out.println(new H5PasswordEncoder().encode(pass, salt, iterations));
    }

}
