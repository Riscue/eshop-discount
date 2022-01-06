package xyz.riscue.eshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private String name;
    private List<RegionPrice> prices;

    private String dekuDealsUrl;
    private String eshopPricesUrl;
}
