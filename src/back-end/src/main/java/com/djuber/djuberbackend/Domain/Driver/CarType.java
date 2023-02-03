package com.djuber.djuberbackend.Domain.Driver;

public enum CarType {
    SEDAN("Sedan", 150.0),
    STATION_WAGON("Station wagon", 180.0),
    VAN("Van", 250.0),
    TRANSPORTER("Transporter", 500.0);

    private String name;
    private Double basePrice;

    CarType(String name, Double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return name;
    }

    public static CarType fromString(String name) {
        for (CarType ct : CarType.values()) {
            if (ct.name.equals(name)) {
                return ct;
            }
        }
        return null;
    }

    public Double getBasePrice() {
        return basePrice;
    }
}
