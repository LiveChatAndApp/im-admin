package cn.wildfirechat.admin.common.strategy;

import cn.wildfirechat.admin.common.consts.ConstDigest;
import com.alibaba.nacos.shaded.com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 密码加密工具
 */
public class PasswordUtil {

    public static final int DEFAULT_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 20;


    public static String generatePassword() {
        String password = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
        String sha256hex = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        return sha256hex;
    }

    /**
     * [H5]加密登录密码
     */
    public static String encode(CharSequence rawPassword, String salt, int iterations) {
        H5PasswordEncoder passwordEncoder = new H5PasswordEncoder();
        passwordEncoder.setSalt(salt);
        passwordEncoder.setIterations(iterations);
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * [H5]加密资金密码
     */
    public static String encodeW(CharSequence rawPassword) {
        H5PasswordEncoder passwordEncoder = new H5PasswordEncoder();
        passwordEncoder.setSalt(ConstDigest.SALT);
        passwordEncoder.setIterations(1);
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * [H5]校验登录密码
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword, String salt, int iterations) {
        H5PasswordEncoder passwordEncoder = new H5PasswordEncoder();
        passwordEncoder.setSalt(salt);
        passwordEncoder.setIterations(iterations);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * [H5]校验资金密码
     */
    public static boolean matchesW(CharSequence rawPassword, String encodedPassword) {
        H5PasswordEncoder passwordEncoder = new H5PasswordEncoder();
        passwordEncoder.setSalt(ConstDigest.SALT);
        passwordEncoder.setIterations(1);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * [Admin]加密登录密码
     */
    public static String encodeA(CharSequence rawPassword) {
        AdminPasswordEncoder passwordEncoder = new AdminPasswordEncoder();
        passwordEncoder.setSalt(ConstDigest.SALT);
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * [Admin]校验登录密码
     */
    public static boolean matchesA(CharSequence rawPassword, String encodedPassword) {
        AdminPasswordEncoder passwordEncoder = new AdminPasswordEncoder();
        passwordEncoder.setSalt(ConstDigest.SALT);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {
        // 明文：abc123
        // 盐：kk5pbdvs
        // 次数：2
        // 密文：992b8863045a09a93cd4692d7ccecbfb

        String pass = "11111111";//1234qwer
        String salt = "SX&SLC`";
        int iterations = 1;

        String sha256hex = Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString();

        System.out.println("sha256hex=="+sha256hex);
        System.out.println("db save=="+PasswordUtil.encode(sha256hex, salt, iterations));
    }

}
