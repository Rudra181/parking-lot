package com.parkinglot;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ParkingLot {
    private final Map<Gate, PriorityQueue<Slot>> sSlots;
    private final Map<Gate, PriorityQueue<Slot>> mSlots;
    private final Map<Gate, PriorityQueue<Slot>> lSlots;
    private final PricingStrategy pricingStrategy;
    private final Map<String, Slot> slotRegistry;

    public ParkingLot(
            Map<Gate, PriorityQueue<Slot>> sSlots,
            Map<Gate, PriorityQueue<Slot>> mSlots,
            Map<Gate, PriorityQueue<Slot>> lSlots,
            PricingStrategy pricingStrategy,
            Map<String, Slot> slotRegistry
    ) {
        this.sSlots = sSlots;
        this.mSlots = mSlots;
        this.lSlots = lSlots;
        this.pricingStrategy = pricingStrategy;
        this.slotRegistry = slotRegistry;
    }

    public Ticket park(VehicleDetails vehicle, LocalTime entryTime, Gate entryGate, SlotType requestedType) {
        validateVehicleTypeCompatibility(vehicle.getType(), requestedType);

        Map<Gate, PriorityQueue<Slot>> selectedSlots = selectSlotsByType(requestedType);
        PriorityQueue<Slot> slotQueue = selectedSlots.get(entryGate);

        if (slotQueue == null || slotQueue.isEmpty()) {
            throw new NoSlotAvailableException("No " + requestedType + " slot available at gate " + entryGate.getId());
        }

        Slot selectedSlot = slotQueue.poll();

        for (PriorityQueue<Slot> otherQueue : selectedSlots.values()) {
            otherQueue.remove(selectedSlot);
        }

        selectedSlot.setAvailable(false);
        selectedSlot.setParkedVehicle(vehicle);

        return new Ticket(selectedSlot.getSlotId(), requestedType, selectedSlot.getLevel(), vehicle, entryGate, entryTime);
    }

    public Map<SlotType, Integer> status() {
        Map<SlotType, Integer> result = new HashMap<>();
        result.put(SlotType.S, countAvailableSlots(sSlots));
        result.put(SlotType.M, countAvailableSlots(mSlots));
        result.put(SlotType.L, countAvailableSlots(lSlots));
        return result;
    }

    public int exit(Ticket ticket, LocalTime exitTime) {
        String slotId = ticket.getSlotId();
        Slot slot = slotRegistry.get(slotId);

        if (slot == null) {
            throw new InvalidTicketException("Slot not found for ticket: " + slotId);
        }

        Map<Gate, PriorityQueue<Slot>> selectedSlots = selectSlotsByType(ticket.getSlotType());

        slot.setAvailable(true);
        slot.setParkedVehicle(null);

        for (PriorityQueue<Slot> slotQueue : selectedSlots.values()) {
            slotQueue.offer(slot);
        }

        double fee = pricingStrategy.calculate(ticket.getEntryTime(), exitTime, slot.getPricePerHour());
        return (int) fee;
    }

    private void validateVehicleTypeCompatibility(VehicleType vehicleType, SlotType slotType) {
        if (slotType == SlotType.S && vehicleType != VehicleType.TWO_WHEELER) {
            throw new IncompatibleVehicleException("Only TWO_WHEELER vehicles fit in S slots");
        }
        if (slotType == SlotType.M && vehicleType == VehicleType.FOUR_L) {
            throw new IncompatibleVehicleException("FOUR_L vehicles cannot fit in M slots");
        }
    }

    private Map<Gate, PriorityQueue<Slot>> selectSlotsByType(SlotType type) {
        return switch (type) {
            case S -> sSlots;
            case M -> mSlots;
            case L -> lSlots;
        };
    }

    private int countAvailableSlots(Map<Gate, PriorityQueue<Slot>> slotMap) {
        int count = 0;
        for (PriorityQueue<Slot> queue : slotMap.values()) {
            for (Slot slot : queue) {
                if (slot.isAvailable()) {
                    count++;
                }
            }
        }
        return count;
    }
}
