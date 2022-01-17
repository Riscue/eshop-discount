package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Alert;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MailTemplates {
    public static final String ALERT_SUBJECT = "Eshop Discount Alert";
    public static final String ALERT_CONTENT = "İndirime giren %s adet oyun bulundu!";
    public static final String ALERT_CONTENT_ITEM = "%s (%s): %s(%%%s) %s";

    public static String prepareContent(List<Alert> alerts) {
        StringBuilder alertItems = new StringBuilder();
        alertItems
                .append(String.format(MailTemplates.ALERT_CONTENT, alerts.size()))
                .append("\n");
        for (Alert alert : alerts) {
            alertItems
                    .append(String.format(MailTemplates.ALERT_CONTENT_ITEM, alert.getName(), alert.getPrice().getRegion(), alert.getPrice().getDiscountedPrice(), AlertUtil.calculateDiscountPercentage(alert.getPrice()), alert.getAlerts()))
                    .append("\n");
        }
        return alertItems.toString();
    }
}
