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

        logger.info("Fetching wishlists");
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

        if (config.isCache()) {
            logger.warn("Running cache only");
        } else {
            logger.info("Fetching DekuDeals game page");
            gameList.forEach(dekuDealsParser::enrich);

            logger.info("Fetching eshop-prices game page");
            gameList.forEach(eshopPricesParser::enrich);
        }

        if (config.isAlert()) {
            logger.info("Checking if any alert rule occured");
            List<Alert> alerts = gameList.stream().map(AlertUtil::checkAlertOccured).filter(Objects::nonNull).collect(Collectors.toList());
            AlertUtil.logAlerts(alerts);
        }

        logger.info("Caching urls to file");
        CacheUtil.cacheSearchResults(gameList);

        logger.info("Exporting to excel");
        ExcelUtil.export(gameList);
    }
}
