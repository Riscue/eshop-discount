package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends TrackableConfig {
    private boolean debug;
    private boolean alert;
    private List<String> wishlist;
    private List<WishlistItem> game;
}
