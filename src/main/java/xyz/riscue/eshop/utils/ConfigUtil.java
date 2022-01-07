package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.config.Config;
import xyz.riscue.eshop.model.config.WishlistItem;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigUtil {

    public static void merge(List<Game> gameList, Config config) {
        for (WishlistItem wishlistItem : config.getGame()) {
            boolean isExist = gameList.stream().anyMatch(g -> g.getName().equals(wishlistItem.getName()));
            if (!isExist) {
                gameList.add(Game.builder()
                        .name(wishlistItem.getName())
                        .build()
                );
            }
        }
    }

    public static void loadAlertsFromConfig(List<Game> gameList, Config config) {
        for (Game game : gameList) {
            WishlistItem wishlistItem = config.getGame().stream().filter(w -> w.getName().equals(game.getName())).findFirst().orElse(null);
            if (wishlistItem != null) {
                game.setDiscountPrice(wishlistItem.getDiscountPrice());
                game.setDiscountPercentage(wishlistItem.getDiscountPercentage());
                game.setAllTimeLow(wishlistItem.getAllTimeLow());
                game.setSignificantDiscount(wishlistItem.getSignificantDiscount());
            }

            if (game.getDiscountPrice() == null) {
                game.setDiscountPrice(config.getDiscountPrice());
            }
            if (game.getDiscountPercentage() == null) {
                game.setDiscountPercentage(config.getDiscountPercentage());
            }
            if (game.getAllTimeLow() == null) {
                game.setAllTimeLow(config.getAllTimeLow());
            }
            if (game.getSignificantDiscount() == null) {
                game.setSignificantDiscount(config.getSignificantDiscount());
            }
        }
    }
}
