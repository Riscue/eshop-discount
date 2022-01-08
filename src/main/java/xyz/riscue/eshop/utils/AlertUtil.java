package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Alert;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.RegionPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        List<String> alertReasons = new ArrayList<>();
        if ((game.getDiscountPrice() != null && checkDiscountPrice(price, game.getDiscountPrice()))) {
            alertReasons.add(String.format("Price under %s", game.getDiscountPrice()));
        }
        if (game.getDiscountPercentage() != null) {
            if (checkDiscountPercentage(price, game.getDiscountPercentage())) {
                alertReasons.add(String.format("Discount percentage %s", calculateDiscountPercentage(price)));
            }
        } else {
            if (game.getSignificantDiscount() != null && game.getSignificantDiscount() && checkDiscountPercentage(price, 25)) {
                alertReasons.add(String.format("Significant discount %s", calculateDiscountPercentage(price)));
            }
        }

        if (game.getAllTimeLow() != null && game.getAllTimeLow() && checkAllTimeLow(price, game.getAllTimeLowPrice())) {
            alertReasons.add("Price is all time low");
        }

        if (!alertReasons.isEmpty()) {
            return Alert.builder()
                    .name(game.getName())
                    .price(price)
                    .dekuDealsUrl(game.getDekuDealsUrl())
                    .eshopPricesUrl(game.getEshopPricesUrl())
                    .alerts(alertReasons)
                    .build();
        }
        return null;
    }

    private static boolean checkDiscountPrice(RegionPrice price, Double discountPrice) {
        return price.getDiscountedPrice() < discountPrice;
    }

    private static boolean checkDiscountPercentage(RegionPrice price, Integer discountPercentage) {
        return calculateDiscountPercentage(price) >= discountPercentage;
    }

    private static double calculateDiscountPercentage(RegionPrice price) {
        double calculatedDiscount = (price.getPrice() - price.getDiscountedPrice()) / price.getPrice() * 100;
        return BigDecimal.valueOf(calculatedDiscount).setScale(0, RoundingMode.CEILING).doubleValue();
    }

    private static boolean checkAllTimeLow(RegionPrice price, RegionPrice allTimeLowPrice) {
        return allTimeLowPrice != null && price.getDiscountedPrice() <= allTimeLowPrice.getDiscountedPrice();
    }
}
