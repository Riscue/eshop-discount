package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import xyz.riscue.eshop.model.Game;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CacheUtil {

    private static final Logger logger = Logger.getLogger(CacheUtil.class);

    @SneakyThrows
    public static void enrichFromCache(List<Game> gameList) {
        List<Game> list = new Yaml().load(new FileReader("cache.yaml"));
        for (Game game : gameList) {
            Game cachedGame = list.stream().filter(g -> g.getName().equals(game.getName())).findFirst().orElse(null);
            if (cachedGame != null) {
                logger.debug(String.format("Game found in cache: %s", game.getName()));
                game.setEshopPricesUrl(cachedGame.getEshopPricesUrl());
            } else {
                game.setEshopPricesUrl(null);
            }
        }
    }

    @SneakyThrows
    public static void cacheSearchResults(List<Game> gameList) {
        new Yaml().dump(gameList, new FileWriter("cache.yaml"));
    }
}
