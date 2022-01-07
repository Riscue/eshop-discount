package xyz.riscue.eshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private String name;
    private RegionPrice price;

    private String dekuDealsUrl;
    private String eshopPricesUrl;
}
