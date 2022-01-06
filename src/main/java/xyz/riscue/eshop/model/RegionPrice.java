package xyz.riscue.eshop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionPrice {
    private Region region;
    private String price;
}
