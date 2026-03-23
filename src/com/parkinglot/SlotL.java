package com.parkinglot;

import java.util.Map;

public class SlotL extends Slot {
    private final double pricePerHour;

    public SlotL(String slotId, int level, Map<Gate, Integer> gateDistances, double pricePerHour) {
        super(slotId, level, gateDistances);
        this.pricePerHour = pricePerHour;
    }

    @Override
    public double getPricePerHour() {
        return pricePerHour;
    }
}
