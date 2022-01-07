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
    private List<RegionPrice> prices;
    private RegionPrice allTimeLowPrice;

    private String dekuDealsUrl;
    private String eshopPricesUrl;
}
