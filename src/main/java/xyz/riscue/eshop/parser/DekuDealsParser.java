package xyz.riscue.eshop.parser;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.utils.HttpRequestUtil;
import xyz.riscue.eshop.utils.SiteHeaderUtil;
import xyz.riscue.eshop.utils.StringUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DekuDealsParser {

    private static final Logger logger = Logger.getLogger(DekuDealsParser.class);

    public String findGameUrl(Game game) {
        if (game.getDekuDealsUrl() != null && !game.getDekuDealsUrl().isEmpty()) {
            return game.getDekuDealsUrl();
        }

        logger.info(String.format("Searching for game url: %s", game.getName()));

        Document document = HttpRequestUtil.get("https://www.dekudeals.com/search?filter[type]=game&q=" + URLEncoder.encode(game.getName(), StandardCharsets.UTF_8), SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getDekuDealsCookies(), SiteHeaderUtil.getDekuDealsHeaders());
        if (document == null) {
            return null;
        }

        Elements items = document.select("main div.search-main a.main-link");
        for (Element item : items) {
            String title = item.text();
            String url = item.absUrl("href");

            if (StringUtil.isEqualEnough(game.getName(), title)) {
                return url;
            }
        }

        return null;
    }

    public List<Game> fetchWishlists(List<String> wishlists) {
        return wishlists.stream().flatMap(wishlist -> parseWishlist(wishlist).stream()).collect(Collectors.toList());
    }

    public List<Game> parseWishlist(String wishlistUrl) {
        ArrayList<Game> games = new ArrayList<>();

        logger.info(String.format("Fetching wishlist: %s", wishlistUrl));

        Document document = HttpRequestUtil.get(wishlistUrl, SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getDekuDealsCookies(), SiteHeaderUtil.getDekuDealsHeaders());
        if (document == null) {
            return games;
        }

        Elements items = document.select("main div.search-main a.main-link");
        for (Element item : items) {
            games.add(
                    Game.builder()
                            .name(item.text())
                            .dekuDealsUrl(item.absUrl("href"))
                            .build()
            );
        }

        logger.info(String.format("%s games found in wishlist", games.size()));
        return games;
    }
}
