package cn.wildfirechat.admin.common.support;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 谷歌身份验证器
 */
public class GoogleAuthenticator {

    // 生成的key长度( Generate secret key length)
    public static final int SECRET_SIZE = 10;

    // 密钥种子
    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcYx";

    // Java实现随机数算法
    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    // 最多可偏移的时间 default 3 - max 17 (from google docs)
    public int window_size = 3;

    /**
     * 最多可偏移的时间，即30s的倍数
     * Set the windows size. This is an integer value representing the number of 30 second windows we allow
     * The bigger the window, the more tolerant of clock skew we are.
     *
     * <p>
     * By default, a new token is generated every 30 seconds by the mobile app.
     * In order to compensate for possible time-skew between the client and the server,
     * we allow an extra token before and after the current time. This allows for a
     * time skew of up to 30 seconds between the authentication server and client. Suppose you
     * experience problems with poor time synchronization. In that case, you can increase the window
     * from its default size of 3 permitted codes (one previous code, the current
     * code, the next code) to 17 permitted codes (the eight previous codes, the current
     * code, and the eight next codes). This will permit a time skew of up to 4 minutes
     * between client and server.
     * </p>
     *
     * @param s window size - must be >=1 and <=17. Other values are ignored
     */
    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17) {
            window_size = s;
        }
    }

    /**
     * 生成一个随机秘钥
     * Generate a random secret key. This must be saved by the server and associated with the
     * users account to verify the code displayed by Google Authenticator.
     * The user must register this secret on their device.
     *
     * @return secret key
     */
    public static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        } catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }

    /**
     * 根据user和secret生成二维码的密钥
     * Return a URL that generates and displays a QR barcode. The user scans this bar code with the
     * Google Authenticator application on their smartphone to register the auth code. They can also
     * manually enter the
     * secret if desired
     *
     * @param user   user id (e.g. fflinstone)
     * @param host   host or system that the code is for (e.g. myapp.com)
     * @param secret the secret that was previously generated for this user
     * @return the URL for the QR code to scan
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
        return String.format(format, user, host, secret);
    }

    /**
     * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
     *
     * @param user   账号
     * @param secret 密钥
     */
    public static String getQRBarcode(String user, String secret) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format(format, user, secret);
    }

    /**
     * 验证code是否合法
     * Check the code entered by the user to see if it is valid
     *
     * @param secret The users secret.
     * @param code   The code displayed on the users device
     */
    public boolean check_code(String secret, String code) {
        return check_code(secret, code, System.currentTimeMillis());
    }

    /**
     * 验证code是否合法
     * Check the code entered by the user to see if it is valid
     *
     * @param secret The users secret.
     * @param code   The code displayed on the users device
     * @param timeMsec The time in msec (System.currentTimeMillis() for example)
     */
    public boolean check_code(String secret, String code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        // convert unix msec time into a 30 second "window"
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / 30L;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verify_code(decodedKey, t + i);
            } catch (Exception e) {
                // Yes, this is bad form - but
                // the exceptions thrown would be rare and a static configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                //return false;
            }
            /*
            if (hash == code) {
                return true;
            }
            */
            System.out.println("code=" + code);
            System.out.println("hash=" + hash);

            if (code.equals(addZero(hash))) {
                return true;
            }
        }
        // The validation code is invalid.
        return false;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }

    /**
     * 网上的大多数实例都采用long型传递code参数，在遇到002345这种口令的时候，会有各种问题，这里要求传入参数使用String，这样00的位不会丢失，
     * 但是后端校验加密出来的串hash是long型有可能会出现023232这种数据，从而会丢失位数，或校验不准。所以写了个方法用0来补位
     */
    private String addZero(long code) {
        return String.format("%06d", code);
    }

    /**
     * 测试方法：
     * <p>
     * 1、执行测试代码中的“genSecretTest”方法，将生成一个URL和一个KEY（用户为testuser），URL打开是一张二维码图片。
     * <p>
     * 2、在手机中下载“GOOGLE身份验证器”。
     * <p>
     * 3、在身份验证器中配置账户，输入账户名（第一步中的用户testuser）、密钥（第一步生成的KEY），选择基于时间。
     * <p>
     * 4、修改测试代码中的“savedSecret”属性，用身份验证器中显示的数字替换测试代码中方法authTest中的code，运行authTest方法即可。
     */
    public static void main(String[] args) {
        // 生成密钥和一个扫描二维码绑定的URL
        String secret = GoogleAuthenticator.generateSecretKey();
        String qrcode = GoogleAuthenticator.getQRBarcodeURL("foxland@163.com", "www.163.com", secret);
        System.out.println("二维码地址:" + qrcode);
        System.out.println("密钥:" + secret);

        // 即secret
        final String savedSecret = "OIUUTKZ5DVQJXWZQ";

        // 根据密钥和验证码进行验证
        String code = "821303";
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5); //should give 5 * 30 seconds of grace...
        boolean r = ga.check_code(savedSecret, code);
        System.out.println("是否正确 = " + r);
    }

}
