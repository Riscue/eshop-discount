package xyz.riscue.eshop.model.config;

import lombok.Data;

@Data
public class TrackableConfig {
    private double discountPrice;
    private int discountPercentage;
    private boolean allTimeLow;
    private boolean significantDiscount;
}
