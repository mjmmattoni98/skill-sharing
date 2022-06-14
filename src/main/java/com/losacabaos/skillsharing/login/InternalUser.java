package com.losacabaos.skillsharing.login;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalUser implements Serializable {
    private String username;
    private String password;
    private boolean skp;
    private int balanceHours;
    private String email;

    public String getUrlMainPage() {
        return "home/list";
    }
}
