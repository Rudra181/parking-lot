package com.parkinglot;

import java.util.Map;

public class SlotS extends Slot {
    private final double pricePerHour;

    public SlotS(String slotId, int level, Map<Gate, Integer> gateDistances, double pricePerHour) {
        super(slotId, level, gateDistances);
        this.pricePerHour = pricePerHour;
    }

    @Override
    public double getPricePerHour() {
        return pricePerHour;
    }
}
