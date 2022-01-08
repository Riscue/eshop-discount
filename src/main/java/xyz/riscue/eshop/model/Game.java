package xyz.riscue.eshop.model;

import lombok.*;
import xyz.riscue.eshop.model.config.TrackableConfig;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends TrackableConfig {
    private String name;
    private Integer metacriticScore;
    private List<RegionPrice> prices;
    private Double allTimeLowPrice;

    private String dekuDealsUrl;
    private String eshopPricesUrl;
}
