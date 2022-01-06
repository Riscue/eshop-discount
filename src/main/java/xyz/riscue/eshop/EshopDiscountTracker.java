package xyz.riscue.eshop;

import org.apache.log4j.Logger;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.config.Config;
import xyz.riscue.eshop.parser.DekuDealsParser;
import xyz.riscue.eshop.parser.EshopPricesParser;
import xyz.riscue.eshop.utils.CacheUtil;
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
        List<Game> gameList = dekuDealsParser.fetchWishlists(config.getDekuDeals().getWishlist());

        logger.info("Enriching eshop-prices urls from cache");
        CacheUtil.enrichFromCache(gameList);

        logger.info("Searching eshop-prices urls");
        gameList.forEach(game -> game.setEshopPricesUrl(eshopPricesParser.findGameUrl(game)));

        logger.info("Caching eshop-prices urls");
        CacheUtil.cacheSearchResults(gameList);

        if (Boolean.parseBoolean(System.getenv("DEBUG"))) {
            logger.warn("Debug mode active, limiting queue to 1");
            gameList = gameList.stream().limit(1).collect(Collectors.toList());
        }

        logger.info("Fetching prices");
        gameList.forEach(game -> game.setPrices(eshopPricesParser.fetchPrice(game)));

        logger.info("Exporting to excel");
        ExcelUtil.export(gameList);
    }
}
