package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends TrackableConfig {
    private boolean debug = false;
    private boolean alert = false;
    private List<String> wishlist;
    private List<WishlistItem> game;
}
