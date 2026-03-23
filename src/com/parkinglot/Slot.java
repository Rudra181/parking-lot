package com.parkinglot;

import java.util.Map;

public abstract class Slot {
    protected String slotId;
    protected int level;
    protected boolean available;
    protected VehicleDetails parkedVehicle;
    protected Map<Gate, Integer> gateDistances;

    public Slot(String slotId, int level, Map<Gate, Integer> gateDistances) {
        this.slotId = slotId;
        this.level = level;
        this.available = true;
        this.parkedVehicle = null;
        this.gateDistances = gateDistances;
    }

    public abstract double getPricePerHour();

    public int distanceFrom(Gate gate) {
        return gateDistances.get(gate);
    }

    public String getSlotId() {
        return slotId;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public VehicleDetails getParkedVehicle() {
        return parkedVehicle;
    }

    public void setParkedVehicle(VehicleDetails parkedVehicle) {
        this.parkedVehicle = parkedVehicle;
    }

    public Map<Gate, Integer> getGateDistances() {
        return gateDistances;
    }
}
