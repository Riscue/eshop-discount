package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends TrackableConfig {
    private boolean cache = false;
    private boolean alert = false;
    private MailConfig mail;
    private List<String> wishlist;
    private List<WishlistItem> game;
}
