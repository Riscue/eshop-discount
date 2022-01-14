package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import xyz.riscue.eshop.model.Alert;
import xyz.riscue.eshop.model.Game;
import xyz.riscue.eshop.model.RegionPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.NONE)
public class AlertUtil {

    private static final Logger logger = Logger.getLogger(AlertUtil.class);

    public static Alert checkAlertOccured(Game game) {
        if (game == null || game.getPrices() == null) {
            return null;
        }

        RegionPrice minPrice = game.getPrices().stream().min(Comparator.comparing(RegionPrice::getDiscountedPrice)).orElse(null);
        if (minPrice == null) {
            return null;
        }

        List<String> alertReasons = new ArrayList<>();

        if (Boolean.TRUE.equals(game.getAlert() != null && game.getAlert().getSale() != null && game.getAlert().getSale()) && !Objects.equals(minPrice.getPrice(), minPrice.getDiscountedPrice())) {
            alertReasons.add("The game is on sale");
        }

        if (game.getAlert() != null && game.getAlert().getDiscountPrice() != null && checkDiscountPrice(minPrice, game.getAlert().getDiscountPrice())) {
            alertReasons.add(String.format("Price under %s", game.getAlert().getDiscountPrice()));
        }

        if (game.getAlert() != null && game.getAlert().getDiscountPercentage() != null) {
            if (checkDiscountPercentage(minPrice, game.getAlert().getDiscountPercentage())) {
                alertReasons.add(String.format("Discount %%%s+", game.getAlert().getDiscountPercentage()));
            }
        } else {
            if (Boolean.TRUE.equals(game.getAlert() != null && game.getAlert().getSignificantDiscount() != null && game.getAlert().getSignificantDiscount()) && checkDiscountPercentage(minPrice, 25)) {
                alertReasons.add(String.format("Significant discount %%%s+", 25));
            }
        }

        if (Boolean.TRUE.equals(game.getAlert() != null && game.getAlert().getAllTimeLow() != null && game.getAlert().getAllTimeLow()) && checkAllTimeLow(minPrice, game.getAllTimeLowPrice())) {
            alertReasons.add("Price is all time low");
        }

        if (!alertReasons.isEmpty()) {
            return Alert.builder()
                    .name(game.getName())
                    .price(minPrice)
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

    private static int calculateDiscountPercentage(RegionPrice price) {
        double calculatedDiscount = (price.getPrice() - price.getDiscountedPrice()) / price.getPrice() * 100;
        return BigDecimal.valueOf(calculatedDiscount).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private static boolean checkAllTimeLow(RegionPrice price, Double allTimeLowPrice) {
        return allTimeLowPrice != null && price.getDiscountedPrice() <= allTimeLowPrice;
    }

    public static void logAlerts(List<Alert> alerts) {
        alerts.stream()
                .sorted(Comparator.comparing(a -> a.getPrice().getRegion()))
                .map(alert -> String.format("Alert -> %s (%s): %s(%%%s) %s", alert.getName(), alert.getPrice().getRegion(), alert.getPrice().getDiscountedPrice(), calculateDiscountPercentage(alert.getPrice()), alert.getAlerts()))
                .forEach(logger::info);
    }
}
