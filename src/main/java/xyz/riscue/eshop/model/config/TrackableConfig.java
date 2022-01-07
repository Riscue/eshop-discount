package xyz.riscue.eshop.model.config;

import lombok.Data;

@Data
public class TrackableConfig {
    private Double discountPrice;
    private Integer discountPercentage;
    private Boolean allTimeLow;
    private Boolean significantDiscount;
}
