package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends TrackableConfig {
    private boolean cache = false;
    private boolean alert = false;
    private MailConfig mail;
    private List<String> wishlist;
    private List<WishlistItem> game;
    private Map<String, Map<String, String>> cookies;
    private Map<String, Map<String, String>> headers;
}
