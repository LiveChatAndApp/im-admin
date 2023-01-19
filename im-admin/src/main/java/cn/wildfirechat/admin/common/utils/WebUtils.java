package cn.wildfirechat.admin.common.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Web页面级别的工具类
 *
 * <pre>
 * 主要提供：
 *     1.页面展示相关的格式化工具
 *     2.sih.tld相关标签库方法
 * </pre>
 */
public class WebUtils {

    /**
     * 删除有风险的标签
     * 1.script
     * 2.iframe
     * 3.link
     * 4.style
     * 5. onXXX事件  (?<=\\s+)on\\w+\\s*=
     * 6.href=
     */
    private static final Pattern htmlHackPtn = Pattern.compile(
            "<script.*?/>|<script.*?/script>|</?iframe.*?>|</?link.*?>|</?style.*?>|&ldquo;|&rdquo;",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern htmlHackRepPtn = Pattern.compile(
            "(?<=\\s+)on\\w+\\s*=|(?<=\\s+)href\\s*=|expression\\s*\\(",
            Pattern.CASE_INSENSITIVE);

    /**
     * 域名后缀
     */
    private final static String DOMAIN = "com|pro|org|net|vip|top|pub|info|world|exchange|io|in|fm|me|cn|hk|tw|us|uk|jp|kr|kh|my|ru|th|vn|pt|ph|id|li";

    /**
     * 域名后缀集合
     */
    private final static Set<String> DOMAIN_SET = new HashSet<>(Arrays.asList(DOMAIN.split("[|]")));

    /**
     * IP格式
     */
    private static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}(\\d{1,3})");

    /**
     * 获取url的顶级域名
     */
    public static String getTopDomain(HttpServletRequest request) {
        return getTopDomain(request.getHeader("host"));
    }

    /**
     * 获取url的顶级域名
     * <pre>
     *     仅支持无path路径的域名地址，如：https://www.baidu.com, 不支持https://www.baidu.com/abc.html
     * </pre>
     */
    public static String getTopDomain(String domain) {
        String host = domain;

        if (host.startsWith("http")) {
            host = host.replaceAll("http://", "").replaceAll("https://", "");
        }

        if (host.endsWith(".") || host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        if (IP_PATTERN.matcher(host).matches()) {
            return host;
        }

        String[] items = host.split("[.]");
        if (items.length >= 2) {
            return items[items.length - 2] + "." + items[items.length - 1];
        }
        return host;
    }

    /**
     * 获取url的顶级域名（仅支持指定域名后缀）
     */
    public static String getTopDomainName(String domain) {
        String host = domain;

        if (host.endsWith(".") || host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }

        if (IP_PATTERN.matcher(host).matches()) {
            return host;
        }

        int index = 0;
        for (; index >= 0; ) {
            index = host.indexOf('.');
            String sub = host.substring(index + 1);
            if (DOMAIN_SET.contains(sub)) {
                return host;
            }
            host = sub;
        }

        return host;
    }

    /**
     * 获取请求域名
     */
    public static String getDomainName(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
    }

    /**
     * 对URI地址编码
     */
    public static String encodeUri(String uri) {
        try {
            return uri == null ? null : URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 对URI地址解码
     */
    public static String decodeUri(String uri) {
        try {
            return uri == null ? null : URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 转义html
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * 过滤html中恶意的脚本和引用
     *
     * <ul>
     * <li>script标签</li>
     * <li>style标签</li>
     * <li>link标签</li>
     * <li>href属性==>data-href</li>
     * <li>onXXX事件属性==>data-onXXX</li>
     * <li>expression(属性方法===>data-expression(</li>
     * </ul>
     */
    public static String escapeHtmlHack(String str) {
        if (StringUtils.isNotBlank(str)) {
            String text = htmlHackRepPtn.matcher(htmlHackPtn.matcher(str).replaceAll("")).replaceAll("data-$0");
            text = text.replaceAll("&ldquo", "\"").replaceAll("&rdquo", "\"");
            return text;
        }
        return str;
    }

    /**
     * 解析UA获取设备信息
     */
    public static String getDevice(HttpServletRequest request) {
        if (request != null) {
            return getDevice(request.getHeader("User-Agent"));
        }
        return null;
    }

    /**
     * 解析UA获取设备信息
     */
    public static String getDevice(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        Browser browser = agent.getBrowser();
        OperatingSystem operatingSystem = agent.getOperatingSystem();

        String device = browser.getGroup().getName();
        if (agent.getBrowserVersion() != null) {
            device += " V" + agent.getBrowserVersion();
        }
        if (operatingSystem.getGroup().getName() != null) {
            device += " (" + operatingSystem.getGroup().getName() + ")";
        }
        return device;
    }

    public static void main(String[] args) {
        String url = "/trade/x.htm?back&dasfasd&";
        System.out.println(url.matches(".*?[?&]back(&.*?)?"));

        String str = "javascripT :www.6006.com/trade/?tradeType=1&commodity=CL<font>\"'";
        System.out.println(encodeUri(str));
        System.out.println(escapeHtml(encodeUri(str)));

        System.out.println(getTopDomainName("https://fk.a.76bao.com/"));
    }

}
