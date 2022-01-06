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
        Map<String, String> headers = new HashMap<>();
        headers.put("authority", "eshop-prices.com");
        headers.put("pragma", "no-cache");
        headers.put("cache-control", "no-cache");
        headers.put("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        headers.put("sec-ch-ua-mobile", "?0");
        headers.put("sec-ch-ua-platform", "\"Linux\"");
        headers.put("dnt", "1");
        headers.put("upgrade-insecure-requests", "1");
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("sec-fetch-site", "same-origin");
        headers.put("sec-fetch-mode", "navigate");
        headers.put("sec-fetch-user", "?1");
        headers.put("sec-fetch-dest", "document");
        headers.put("referer", "https://eshop-prices.com");
        headers.put("accept-language", "tr,en;q=0.9,tr-TR;q=0.8");
        return headers;
    }

    public static Map<String, String> getEshopPricesCookies() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("_ga", "GA1.2.1883035158.1640600984");
        cookies.put("_gid", "GA1.2.1796017944.1641204144");
        cookies.put("__gads", "ID=010ef5a65904fd29-224f245816cd002c:T=1641373914:RT=1641373914:S=ALNI_Maw8JRulj2LjTLQW3_kanzHab4QdA");
        cookies.put("cf_chl_2", "f342320d4499b24");
        cookies.put("cf_chl_prog", "x11");
        cookies.put("cf_clearance", "Ni77yScIpYmgXZixVWq3BNNswqZ89uJkKnCRGqviw0c-1641467405-0-250");
        cookies.put("_gat_gtag_UA_55187579_10", "1");
        cookies.put("_eshop_prices_session", "LrrQG6ewB%2BK0KBe4suCmklxFhv4y%2BUoSibE7C%2F1hbbtWBg2duxoWwSIw4k9wlxZgKGJhefM6OQ81i9oLnPKtEeqZVUr2wIYxpYrLey069jMlbM3kdC6bh57rkh30eO9zFcjNfF4qfsM5tHFldTvbmjSfcypp48lEcUE%2Fib%2FgnYY9EtnQ%2BKC02vg3R6Z7ctX9TW3kcneItYxlw1YIyi7WOcn6%2F4lOYx7MwtTPDwfmc3nqtj%2Bbw8z8CisMda7Zp8566sBWIfQ%2BFKBHlpCZJL81ODsYS4DamQ%2FCvro89e0%3D--VVK8GjzvqVIHUKKy--R5B9hvabh7ocpTSvsRr97A%3D%3D");
        return cookies;
    }

    public static Map<String, String> getDekuDealsHeaders() {
        return new HashMap<>();
    }

    public static Map<String, String> getDekuDealsCookies() {
        return new HashMap<>();
    }
}
