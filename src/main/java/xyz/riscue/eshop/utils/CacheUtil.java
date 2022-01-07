package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.cache.CacheContainer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CacheUtil {

    private static final Logger logger = Logger.getLogger(CacheUtil.class);

    @SneakyThrows
    public static void enrichFromCache(List<Game> gameList) {
        String fileName = "cache.yaml";
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        CacheContainer gameListCacheContainer = new Yaml(new Constructor(CacheContainer.class)).load(new FileReader(file));
        if (gameListCacheContainer == null) {
            return;
        }

        List<Game> list = gameListCacheContainer.getCache();
        for (Game game : gameList) {
            Game cachedGame = list.stream().filter(g -> g.getName().equals(game.getName())).findFirst().orElse(null);
            if (cachedGame != null) {
                logger.info(String.format("Game found in cache: %s", game.getName()));
                game.setDekuDealsUrl(cachedGame.getEshopPricesUrl());
                game.setEshopPricesUrl(cachedGame.getEshopPricesUrl());
            }
        }
    }

    @SneakyThrows
    public static void cacheSearchResults(List<Game> gameList) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new MapRepresenter();

        Yaml yaml = new Yaml(representer, options);
        yaml.dump(new CacheContainer(gameList), new FileWriter("cache.yaml"));
    }

    public static class MapRepresenter extends Representer {
        @Override
        protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
            if (!classTags.containsKey(javaBean.getClass())) {
                addClassTag(javaBean.getClass(), Tag.MAP);
            }

            return super.representJavaBean(properties, javaBean);
        }

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
            if (propertyValue == null) {
                return null;
            } else {
                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }
}
