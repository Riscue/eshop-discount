package xyz.riscue.eshop;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import xyz.riscue.eshop.model.config.Config;

import java.io.File;
import java.io.FileReader;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    @SneakyThrows
    public static void main(String[] args) {
        Config config = parseConfig();
        EshopDiscountTracker eshopDiscountTracker = new EshopDiscountTracker(config);
        eshopDiscountTracker.track();
    }

    @SneakyThrows
    private static Config parseConfig() {
        String configFile = System.getenv("CONFIG_FILE");
        if (configFile == null || configFile.isEmpty()) {
            logger.warn("'CONFIG_FILE' env is not set");
            configFile = "config.yaml";
        }

        File file = new File(configFile);
        if (!file.exists()) {
            logger.error(String.format("'%s' does not exists", file.getAbsolutePath()));
            System.exit(-1);
        }

        FileReader fileReader = new FileReader(file);
        Config config = new Yaml().loadAs(fileReader, Config.class);

        if (config == null) {
            logger.error(String.format("'%s' is empty", file.getAbsolutePath()));
            System.exit(-1);
        }

        return config;
    }
}
