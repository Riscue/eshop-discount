package xyz.riscue.eshop;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.cache.CacheContainer;
import xyz.riscue.eshop.model.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Config config = parseConfig();
        List<Game> cache = parseCache();
        EshopDiscountTracker eshopDiscountTracker = new EshopDiscountTracker(config, cache);
        eshopDiscountTracker.track();
    }

    private static Config parseConfig() {
        String configFile = System.getenv("CONFIG_FILE");
        if (configFile == null || configFile.isEmpty()) {
            logger.warn("'CONFIG_FILE' env is not set");
            configFile = "config.yaml";
        }

        File file = new File(configFile);

        try {
            FileReader fileReader = new FileReader(file);
            Config config = new Yaml().loadAs(fileReader, Config.class);

            if (config == null) {
                logger.error(String.format("'%s' is empty", file.getAbsolutePath()));
                System.exit(-1);
            }

            return config;
        } catch (FileNotFoundException e) {
            logger.error(String.format("'%s' does not exists", file.getAbsolutePath()));
            System.exit(-1);
        }

        return null;
    }

    private static List<Game> parseCache() {
        String cacheFile = System.getenv("CACHE_FILE");
        if (cacheFile == null || cacheFile.isEmpty()) {
            logger.warn("'CACHE_FILE' env is not set");
            cacheFile = "cache.yaml";
        }

        File file = new File(cacheFile);

        try {
            CacheContainer gameListCacheContainer = new Yaml(new Constructor(CacheContainer.class)).load(new FileReader(file));
            if (gameListCacheContainer == null) {
                return new ArrayList<>();
            }

            return gameListCacheContainer.getCache();
        } catch (FileNotFoundException e) {
            logger.debug(String.format("'%s' does not exists", file.getAbsolutePath()));
        }

        return new ArrayList<>();
    }
}
