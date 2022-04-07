package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SiteHeaderUtil {

    private static final String USER_AGENT = "Eshop-Discount";

    private static final Map<String, Map<String, String>> headers = new HashMap<>();
    private static final Map<String, Map<String, String>> cookies = new HashMap<>();

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static Map<String, String> getHeaders(String domain) {
        return headers.getOrDefault(domain, new HashMap<>());
    }

    public static Map<String, String> getCookies(String domain) {
        return cookies.getOrDefault(domain, new HashMap<>());
    }

    public static void setHeaders(Map<String, Map<String, String>> headers) {
        if (headers != null) {
            SiteHeaderUtil.headers.putAll(headers);
        }
    }

    public static void setCookies(Map<String, Map<String, String>> cookies) {
        if (cookies != null) {
            SiteHeaderUtil.cookies.putAll(cookies);
        }
    }
}
