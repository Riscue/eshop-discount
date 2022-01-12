package xyz.riscue.eshop.model.config;

import lombok.Data;

import java.util.List;

@Data
public class MailConfig {
    private boolean disabled;
    private String host;
    private String port;
    private String username;
    private String password;

    private String from;
    private List<String> to;
    private List<String> cc;
}
