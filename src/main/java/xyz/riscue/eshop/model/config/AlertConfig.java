package xyz.riscue.eshop.model.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertConfig {
    private Boolean disabled;
    private Boolean sale;
    private Double discountPrice;
    private Integer discountPercentage;
    private Boolean allTimeLow;
    private Boolean significantDiscount;
}
