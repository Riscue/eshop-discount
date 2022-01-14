package xyz.riscue.eshop.model.config;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Config {
    private boolean cache = false;
    private AlertConfig alert;
    private MailConfig mail;
    private List<String> wishlist;
    private List<WishlistItem> game;
    private Map<String, Map<String, String>> cookies;
    private Map<String, Map<String, String>> headers;
}
