package xyz.riscue.eshop.model.config;

import lombok.Data;

import java.util.List;

@Data
public class Config {
    private boolean debug;
    private EshopPrices eshopPrices;
    private DekuDeals dekuDeals;
    private List<CustomWishlist> customWishlist;
}
