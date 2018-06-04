package com.example.demo.domain.enums;

public enum EventType {

    TRADE_SHOW("TS"),
    BUSINESS_DINNER("BD");

    private String value;

    private EventType(String value){
        this.value = value;

    }

    public String getValue() {
        return value;
    }
}
