package com.djuber.djuberbackend.Domain.Driver;

public enum CarType {
    SEDAN("Sedan"),
    STATION_WAGON("Station wagon"),
    VAN("Van"),
    TRANSPORTER("Transporter");

    private String name;

    CarType(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
