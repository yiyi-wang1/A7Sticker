package com.example.a7sticker;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String token;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public User(){
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
