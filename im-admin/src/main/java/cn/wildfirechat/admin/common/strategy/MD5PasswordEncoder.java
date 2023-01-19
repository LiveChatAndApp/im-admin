package cn.wildfirechat.admin.common.strategy;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加盐加密策略
 */
public class MD5PasswordEncoder implements PasswordEncoder {

    // 加密盐
    private String salt = "";
    // 加密次数
    private int iterations = 1;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword != null ? digest(rawPassword.toString(), salt, iterations) : null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword != null) {
            String encrypted = digest(rawPassword.toString(), salt, iterations);
            return encrypted.equals(encodedPassword);
        }
        return false;
    }

    /**
     * 加密过程
     */
    public String digest(String password, String salt, int iterations) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");

            if (salt != null) {
                digest.update(salt.getBytes(StandardCharsets.UTF_8));
            }

            byte[] result = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }

            return Hex.encodeHexString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
