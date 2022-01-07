package xyz.riscue.eshop.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
@Data
public class WishlistItem extends TrackableConfig {
    private String name;
}
