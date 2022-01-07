package xyz.riscue.eshop;

import org.apache.log4j.Logger;
import xyz.riscue.eshop.model.Alert;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.config.Config;
import xyz.riscue.eshop.parser.DekuDealsParser;
import xyz.riscue.eshop.parser.EshopPricesParser;
import xyz.riscue.eshop.utils.AlertUtil;
import xyz.riscue.eshop.utils.CacheUtil;
import xyz.riscue.eshop.utils.ConfigUtil;
import xyz.riscue.eshop.utils.ExcelUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EshopDiscountTracker {

    private static final Logger logger = Logger.getLogger(EshopDiscountTracker.class);

    private final Config config;
    private final List<Game> cache;

    public EshopDiscountTracker(Config config, List<Game> cache) {
        this.config = config;
        this.cache = cache;
    }

    public void track() {
        DekuDealsParser dekuDealsParser = new DekuDealsParser();
        EshopPricesParser eshopPricesParser = new EshopPricesParser();

        logger.info("Fetching wishlist");
        List<Game> gameList = dekuDealsParser.fetchWishlists(config.getWishlist());

        logger.info("Merge DekuDeals Wishlist and Custom Wishlist");
        ConfigUtil.merge(gameList, config);

        logger.info("Load alerts from Config");
        gameList.forEach(game -> ConfigUtil.loadAlertsFromConfig(game, config));

        logger.info("Enriching urls from cache");
        gameList.forEach(game -> CacheUtil.enrichFromCache(game, cache));

        logger.info("Searching DekuDeals urls");
        gameList.forEach(game -> game.setDekuDealsUrl(dekuDealsParser.findGameUrl(game)));

        logger.info("Searching eshop-prices urls");
        gameList.forEach(game -> game.setEshopPricesUrl(eshopPricesParser.findGameUrl(game)));

        if (config.isDebug()) {
            logger.warn("Debug mode active, limiting queue to 1");
            gameList.stream().filter(game -> game.getName().equals("The Witcher 3: Wild Hunt")).collect(Collectors.toList()).forEach(game -> game.setPrices(eshopPricesParser.fetchPrice(game)));
        } else {
            logger.info("Fetching prices");
            gameList.forEach(game -> game.setPrices(eshopPricesParser.fetchPrice(game)));
        }

        logger.info("Checking if any alert rule occured");
        List<Alert> alerts = gameList.stream().map(AlertUtil::checkAlertOccured).filter(Objects::nonNull).collect(Collectors.toList());
        alerts.forEach(alert -> logger.info(String.format("Alert -> %s: %s", alert.getName(), alert.getPrice().getDiscountedPrice())));

        logger.info("Caching urls to file");
        CacheUtil.cacheSearchResults(gameList);

        logger.info("Exporting to excel");
        ExcelUtil.export(gameList);
    }
}
