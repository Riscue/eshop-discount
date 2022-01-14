package xyz.riscue.eshop.model.config;

import lombok.Data;

@SuppressWarnings("unused")
@Data
public class WishlistItem {
    private String name;
    private AlertConfig alert;
}
