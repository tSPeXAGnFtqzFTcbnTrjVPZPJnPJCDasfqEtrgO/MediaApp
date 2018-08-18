package com.example.andeptrai.myapplication.constant;

public enum ActionBroadCast {
    CURSEEK();

    private String name;

    ActionBroadCast() {
        this.name = this.name();
    }

    public String getName() {
        return name;
    }
}
