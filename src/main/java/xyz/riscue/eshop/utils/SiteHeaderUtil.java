package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SiteHeaderUtil {

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36";

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static Map<String, String> getEshopPricesHeaders() {
        return new HashMap<>();
    }

    public static Map<String, String> getEshopPricesCookies() {
        return new HashMap<>();
    }

    public static Map<String, String> getDekuDealsHeaders() {
        return new HashMap<>();
    }

    public static Map<String, String> getDekuDealsCookies() {
        return new HashMap<>();
    }
}
