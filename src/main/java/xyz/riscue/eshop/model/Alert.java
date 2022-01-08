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
public class Alert {
    private String name;
    private RegionPrice price;

    private List<String> alerts;

    private String dekuDealsUrl;
    private String eshopPricesUrl;
}
