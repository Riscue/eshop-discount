package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.config.AlertConfig;
import xyz.riscue.eshop.model.config.Config;
import xyz.riscue.eshop.model.config.WishlistItem;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigUtil {

    public static void merge(List<Game> gameList, Config config) {
        if (config.getGame() == null) {
            return;
        }

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

    public static void loadAlertsFromConfig(Game game, Config config) {
        if (config.getGame() != null) {
            WishlistItem wishlistItem = config.getGame().stream().filter(w -> w.getName().equals(game.getName())).findFirst().orElse(null);
            if (wishlistItem != null && wishlistItem.getAlert() != null) {
                game.setAlert(AlertConfig.builder()
                        .disabled(wishlistItem.getAlert().getDisabled())
                        .sale(wishlistItem.getAlert().getSale())
                        .discountPrice(wishlistItem.getAlert().getDiscountPrice())
                        .discountPercentage(wishlistItem.getAlert().getDiscountPercentage())
                        .allTimeLow(wishlistItem.getAlert().getAllTimeLow())
                        .significantDiscount(wishlistItem.getAlert().getSignificantDiscount())
                        .build());
            }
        }

        if (game.getAlert() == null) {
            game.setAlert(new AlertConfig());
        }
        if (game.getAlert().getDisabled() == null) {
            game.getAlert().setDisabled(config.getAlert().getDisabled());
        }
        if (game.getAlert().getSale() == null) {
            game.getAlert().setSale(config.getAlert().getSale());
        }
        if (game.getAlert().getDiscountPrice() == null) {
            game.getAlert().setDiscountPrice(config.getAlert().getDiscountPrice());
        }
        if (game.getAlert().getDiscountPercentage() == null) {
            game.getAlert().setDiscountPercentage(config.getAlert().getDiscountPercentage());
        }
        if (game.getAlert().getAllTimeLow() == null) {
            game.getAlert().setAllTimeLow(config.getAlert().getAllTimeLow());
        }
        if (game.getAlert().getSignificantDiscount() == null) {
            game.getAlert().setSignificantDiscount(config.getAlert().getSignificantDiscount());
        }
    }
}
