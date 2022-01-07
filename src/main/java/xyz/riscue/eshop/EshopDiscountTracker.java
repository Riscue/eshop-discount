package xyz.riscue.eshop;

import org.apache.log4j.Logger;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.config.Config;
import xyz.riscue.eshop.parser.DekuDealsParser;
import xyz.riscue.eshop.parser.EshopPricesParser;
import xyz.riscue.eshop.utils.CacheUtil;
import xyz.riscue.eshop.utils.ConfigUtil;
import xyz.riscue.eshop.utils.ExcelUtil;

import java.util.List;
import java.util.stream.Collectors;

public class EshopDiscountTracker {

    private static final Logger logger = Logger.getLogger(EshopDiscountTracker.class);

    private final Config config;

    public EshopDiscountTracker(Config config) {
        this.config = config;
    }

    public void track() {
        DekuDealsParser dekuDealsParser = new DekuDealsParser();
        EshopPricesParser eshopPricesParser = new EshopPricesParser();

        logger.info("Fetching wishlist");
        List<Game> gameList = dekuDealsParser.fetchWishlists(config.getWishlist());

        logger.info("Merge DekuDeals Wishlist and Custom Wishlist");
        ConfigUtil.merge(gameList, config);

        logger.info("Load alerts from Config");
        ConfigUtil.loadAlertsFromConfig(gameList, config);

        logger.info("Enriching urls from cache");
        CacheUtil.enrichFromCache(gameList);

        logger.info("Searching DekuDeals urls");
        gameList.forEach(game -> game.setDekuDealsUrl(dekuDealsParser.findGameUrl(game)));

        logger.info("Searching eshop-prices urls");
        gameList.forEach(game -> game.setEshopPricesUrl(eshopPricesParser.findGameUrl(game)));

        if (config.getDebug()) {
            logger.warn("Debug mode active, limiting queue to 1");
            gameList.stream().filter(game -> game.getName().equals("The Witcher 3: Wild Hunt")).collect(Collectors.toList()).forEach(game -> game.setPrices(eshopPricesParser.fetchPrice(game)));
        } else {
            logger.info("Fetching prices");
            gameList.forEach(game -> game.setPrices(eshopPricesParser.fetchPrice(game)));
        }

        logger.info("Caching urls to file");
        CacheUtil.cacheSearchResults(gameList);

        logger.info("Exporting to excel");
        ExcelUtil.export(gameList);
    }
}
