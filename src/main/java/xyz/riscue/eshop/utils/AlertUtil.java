package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Alert;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.RegionPrice;

import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.NONE)
public class AlertUtil {

    public static Alert checkAlertOccured(Game game) {
        if (game == null || game.getPrices() == null) {
            return null;
        }

        RegionPrice price = game.getPrices().stream().min(Comparator.comparing(RegionPrice::getPrice)).orElse(null);
        if (price == null) {
            return null;
        }

        if (game.getDiscountPrice() != null && checkDiscountPrice(price, game.getDiscountPrice()) ||
                game.getDiscountPercentage() != null && checkDiscountPercentage(price, game.getDiscountPercentage()) ||
                game.getAllTimeLow() != null && game.getAllTimeLow() && checkAllTimeLow(price, game.getAllTimeLowPrice()) ||
                game.getSignificantDiscount() != null && game.getSignificantDiscount() && checkDiscountPercentage(price, 25)) {
            return Alert.builder()
                    .name(game.getName())
                    .price(price)
                    .dekuDealsUrl(game.getDekuDealsUrl())
                    .eshopPricesUrl(game.getEshopPricesUrl())
                    .build();
        }

        return null;
    }

    private static boolean checkDiscountPrice(RegionPrice price, Double discountPrice) {
        return price.getDiscountedPrice() < discountPrice;
    }

    private static boolean checkDiscountPercentage(RegionPrice price, Integer discountPercentage) {
        return (price.getDiscountedPrice() / price.getPrice() * 100) >= discountPercentage;
    }

    private static boolean checkAllTimeLow(RegionPrice price, RegionPrice allTimeLowPrice) {
        return allTimeLowPrice != null && price.getDiscountedPrice() <= allTimeLowPrice.getDiscountedPrice();
    }
}
