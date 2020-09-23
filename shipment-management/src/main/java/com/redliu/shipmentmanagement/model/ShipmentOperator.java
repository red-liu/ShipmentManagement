package com.redliu.shipmentmanagement.model;

public enum  ShipmentOperator {
    Initial("Initial"),
    Split("Split"),
    Merge("Merge");

    private String name;

    private ShipmentOperator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
