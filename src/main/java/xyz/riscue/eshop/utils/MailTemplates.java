package xyz.riscue.eshop.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.Alert;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MailTemplates {
    public static final String ALERT_SUBJECT = "Eshop Discount Alert";
    public static final String ALERT_CONTENT = "İndirime giren %s adet oyun bulundu!\n%s";
    public static final String ALERT_CONTENT_ITEM = "%s: %s (%s) %s\n";

    public static String prepareContent(List<Alert> alerts) {
        StringBuilder alertItems = new StringBuilder();
        for (Alert alert : alerts) {
            alertItems.append(String.format(MailTemplates.ALERT_CONTENT_ITEM, alert.getName(), alert.getPrice().getDiscountedPrice(), alert.getPrice().getRegion(), alert.getAlerts()));
        }
        return String.format(MailTemplates.ALERT_CONTENT, alerts.size(), alertItems);
    }
}
