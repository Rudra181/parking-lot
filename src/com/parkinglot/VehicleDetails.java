package com.parkinglot;

public class VehicleDetails {
    private final String registrationNumber;
    private final VehicleType type;
    private final String ownerName;

    public VehicleDetails(String registrationNumber, VehicleType type, String ownerName) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.ownerName = ownerName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
