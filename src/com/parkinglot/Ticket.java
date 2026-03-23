package com.parkinglot;

import java.time.LocalTime;

public class Ticket {
    private final String slotId;
    private final SlotType slotType;
    private final int level;
    private final VehicleDetails vehicle;
    private final Gate entryGate;
    private final LocalTime entryTime;

    public Ticket(String slotId, SlotType slotType, int level, VehicleDetails vehicle, Gate entryGate, LocalTime entryTime) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.level = level;
        this.vehicle = vehicle;
        this.entryGate = entryGate;
        this.entryTime = entryTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getLevel() {
        return level;
    }

    public VehicleDetails getVehicle() {
        return vehicle;
    }

    public Gate getEntryGate() {
        return entryGate;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }
}
