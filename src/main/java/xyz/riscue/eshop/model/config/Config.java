package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Config extends TrackableConfig {
    private Boolean debug;
    private Boolean alert;
    private List<String> wishlist;
    private List<WishlistItem> game;
}
