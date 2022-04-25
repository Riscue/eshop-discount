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

    private static final String DOMAIN = "dekudeals.com";

    public String findGameUrl(Game game) {
        if (StringUtil.notEmpty(game.getDekuDealsUrl())) {
            return game.getDekuDealsUrl();
        }

        logger.info(String.format("Searching for game url: %s", game.getName()));

        Document document = HttpRequestUtil.get("https://www.dekudeals.com/search?filter[type]=game&q=" + URLEncoder.encode(game.getName(), StandardCharsets.UTF_8), SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getCookies(DOMAIN), SiteHeaderUtil.getHeaders(DOMAIN));
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

    @SuppressWarnings("unused")
    public void enrich(Game game) {
        if (game.getDekuDealsUrl() == null) {
            return;
        }

        logger.info(String.format("Fetching data for game: %s", game.getName()));

        Document document = HttpRequestUtil.get(game.getDekuDealsUrl(), SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getCookies(DOMAIN), SiteHeaderUtil.getHeaders(DOMAIN));
        if (document == null) {
            return;
        }

        try {
            Elements allTimeLow = document.select("#price-history > table > tbody > tr:nth-child(2) > td.text-right.pl-3");
            if (allTimeLow.size() == 1) {
                game.setAllTimeLowPrice(Double.parseDouble(allTimeLow.get(0).text().replaceAll("[^\\d.]+", "").trim()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<Game> fetchLists(List<String> listUrls) {
        return listUrls.stream().flatMap(listUrl -> parseList(listUrl).stream()).collect(Collectors.toList());
    }

    public List<Game> parseList(String listUrl) {
        ArrayList<Game> games = new ArrayList<>();

        logger.info(String.format("Fetching list: %s", listUrl));

        Document document = HttpRequestUtil.get(listUrl, SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getCookies(DOMAIN), SiteHeaderUtil.getHeaders(DOMAIN));
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

        logger.info(String.format("%s games found in list", games.size()));
        return games;
    }
}
