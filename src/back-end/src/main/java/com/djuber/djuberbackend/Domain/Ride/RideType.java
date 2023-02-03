package com.djuber.djuberbackend.Domain.Ride;

import com.djuber.djuberbackend.Domain.Driver.CarType;

public enum RideType {
    SINGLE("Single"),
    SHARE_RIDE("Share ride");

    private String name;
    RideType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static RideType fromString(String name) {
        for (RideType rt : RideType.values()) {
            if (rt.name.equals(name)) {
                return rt;
            }
        }
        return null;
    }
}
