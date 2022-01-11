package xyz.riscue.eshop.utils;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@NoArgsConstructor(access = AccessLevel.NONE)
public class HttpRequestUtil {

    private static final Logger logger = Logger.getLogger(HttpRequestUtil.class);

    private static final RateLimiter throttle = RateLimiter.create(0.5);

    @SneakyThrows(value = InterruptedException.class)
    public static Document get(String url, String userAgent, Map<String, String> cookies, Map<String, String> headers) {
        throttle.acquire();

        logger.debug(String.format("Fetching: %s", url));

        int retry = 0;
        do {
            try {
                return Jsoup
                        .connect(url)
                        .userAgent(userAgent)
                        .cookies(cookies)
                        .headers(headers)
                        .get();
            } catch (HttpStatusException e) {
                logger.error(e.getMessage(), e);
                if (e.getMessage().contains("Status=429")) {
                    Thread.sleep(10000);
                    retry++;
                    logger.warn("Retrying...");
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } while (retry < 3);

        return null;
    }
}
