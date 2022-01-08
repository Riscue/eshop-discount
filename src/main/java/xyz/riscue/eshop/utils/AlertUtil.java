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

@NoArgsConstructor(access = AccessLevel.NONE)
public class AlertUtil {

    private static final Logger logger = Logger.getLogger(AlertUtil.class);

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
                alertReasons.add(String.format("Discount percentage %s", game.getDiscountPercentage()));
            }
        } else {
            if (game.getSignificantDiscount() != null && game.getSignificantDiscount() && checkDiscountPercentage(price, 25)) {
                alertReasons.add(String.format("Significant discount %s", 25));
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
        return BigDecimal.valueOf(calculatedDiscount).setScale(0, RoundingMode.HALF_UP).doubleValue();
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
