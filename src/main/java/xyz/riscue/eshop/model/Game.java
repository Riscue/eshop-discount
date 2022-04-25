package xyz.riscue.eshop.model;

import lombok.*;
import xyz.riscue.eshop.model.config.AlertConfig;

import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @EqualsAndHashCode.Include
    private String name;

    private Integer metacriticScore;
    private List<RegionPrice> prices;
    private Double allTimeLowPrice;

    private String dekuDealsUrl;
    private String eshopPricesUrl;

    private AlertConfig alert;
}
