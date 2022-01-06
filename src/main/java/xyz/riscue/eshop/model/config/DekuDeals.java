package xyz.riscue.eshop.model.config;

import lombok.Data;

import java.util.List;

@SuppressWarnings("unused")
@Data
public class DekuDeals {
    private List<String> wishlist;
    private boolean alert;
}
