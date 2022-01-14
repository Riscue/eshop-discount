package xyz.riscue.eshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.riscue.eshop.model.config.AlertConfig;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String name;
    private Integer metacriticScore;
    private List<RegionPrice> prices;
    private Double allTimeLowPrice;

    private String dekuDealsUrl;
    private String eshopPricesUrl;

    private AlertConfig alert;
}
