package xyz.riscue.eshop.parser;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.Region;
import xyz.riscue.eshop.model.RegionPrice;
import xyz.riscue.eshop.utils.HttpRequestUtil;
import xyz.riscue.eshop.utils.SiteHeaderUtil;
import xyz.riscue.eshop.utils.StringUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EshopPricesParser {

    private static final Logger logger = Logger.getLogger(EshopPricesParser.class);

    public String findGameUrl(Game game) {
        if (game.getEshopPricesUrl() != null && !game.getEshopPricesUrl().isEmpty()) {
            return game.getEshopPricesUrl();
        }

        logger.info(String.format("Searching for game url: %s", game.getName()));

        Document document = HttpRequestUtil.get("https://eshop-prices.com/games?q=" + URLEncoder.encode(game.getName(), StandardCharsets.UTF_8), SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getEshopPricesCookies(), SiteHeaderUtil.getEshopPricesHeaders());
        if (document == null) {
            return null;
        }

        Elements items = document.select(".games-list > a.games-list-item");
        for (Element item : items) {
            String title = item.select(".games-list-item-title h5").text();
            String url = item.absUrl("href");

            if (StringUtil.isEqualEnough(game.getName(), title)) {
                return url;
            }
        }

        return null;
    }

    public List<RegionPrice> fetchPrice(Game game) {
        List<RegionPrice> priceList = new ArrayList<>();

        if (game.getEshopPricesUrl() == null) {
            return priceList;
        }

        logger.info(String.format("Fetching price for game: %s", game.getName()));

        Document document = HttpRequestUtil.get(game.getEshopPricesUrl() + "?currency=USD", SiteHeaderUtil.getUserAgent(), SiteHeaderUtil.getEshopPricesCookies(), SiteHeaderUtil.getEshopPricesHeaders());
        if (document == null) {
            return priceList;
        }

        Elements items = document.select("table.prices-table > tbody > tr.pointer");
        for (Element item : items) {
            String regionName = item.select("td:nth-child(2)").text().replace(" ", "_").toUpperCase();
            String price = item.select("td:nth-child(4) div.discounted del").text().replace("$", "").trim();
            String discountedPrice = item.select("td:nth-child(4)").text().replace(price, "").replace("$", "").trim();

            if (item.select("td:nth-child(4) div.discounted del").isEmpty()) {
                price = discountedPrice;
            }

            Region region = Region.find(regionName);
            if (region != null) {
                priceList.add(RegionPrice.builder()
                        .region(region)
                        .price(Double.parseDouble(price))
                        .discountedPrice(Double.parseDouble(discountedPrice))
                        .build());
            }
        }
        return priceList;
    }
}
