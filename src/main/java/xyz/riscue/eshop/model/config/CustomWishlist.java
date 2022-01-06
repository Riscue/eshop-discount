package xyz.riscue.eshop.model.config;

import lombok.Data;

@SuppressWarnings("unused")
@Data
public class CustomWishlist {
    private String name;
    private String targetPrice;
    private int priceDropPercentage;
    private boolean allTimeLow;
    private boolean significantPriceDrop;
}
