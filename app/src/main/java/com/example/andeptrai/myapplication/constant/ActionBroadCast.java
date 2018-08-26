package com.example.andeptrai.myapplication.constant;

public enum ActionBroadCast {
    CURSEEK(),
    PLAY(),
    PAUSE(),
    PREV(),
    NEXT(),
    STOP(),
    UPDATE_LIST_SHUFFLE();

    private String name;

    ActionBroadCast() {
        this.name = this.name();
    }

    public String getName() {
        return name;
    }
}
