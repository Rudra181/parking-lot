package com.parkinglot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ParkingLotFactory {
    public static ParkingLot create(
            int levels,
            Map<Integer, Map<SlotType, Integer>> levelSlotMapping,
            List<Gate> gates,
            Map<Gate, Map<String, Integer>> gateSlotDistances,
            Map<SlotType, Double> pricesPerHour,
            PricingStrategy pricingStrategy
    ) {
        Map<String, Slot> slotRegistry = new HashMap<>();

        Map<Gate, PriorityQueue<Slot>> sSlots = initializeGateSlotQueues(gates);
        Map<Gate, PriorityQueue<Slot>> mSlots = initializeGateSlotQueues(gates);
        Map<Gate, PriorityQueue<Slot>> lSlots = initializeGateSlotQueues(gates);

        for (int level = 0; level < levels; level++) {
            Map<SlotType, Integer> slotCountsByType = levelSlotMapping.get(level);

            if (slotCountsByType == null) continue;

            int sIndex = 0;
            if (slotCountsByType.containsKey(SlotType.S)) {
                for (int i = 0; i < slotCountsByType.get(SlotType.S); i++) {
                    Slot slot = createSlotS(level, sIndex, gates, gateSlotDistances, pricesPerHour.get(SlotType.S));
                    slotRegistry.put(slot.getSlotId(), slot);
                    addSlotToGateQueues(sSlots, slot, gates);
                    sIndex++;
                }
            }

            int mIndex = 0;
            if (slotCountsByType.containsKey(SlotType.M)) {
                for (int i = 0; i < slotCountsByType.get(SlotType.M); i++) {
                    Slot slot = createSlotM(level, mIndex, gates, gateSlotDistances, pricesPerHour.get(SlotType.M));
                    slotRegistry.put(slot.getSlotId(), slot);
                    addSlotToGateQueues(mSlots, slot, gates);
                    mIndex++;
                }
            }

            int lIndex = 0;
            if (slotCountsByType.containsKey(SlotType.L)) {
                for (int i = 0; i < slotCountsByType.get(SlotType.L); i++) {
                    Slot slot = createSlotL(level, lIndex, gates, gateSlotDistances, pricesPerHour.get(SlotType.L));
                    slotRegistry.put(slot.getSlotId(), slot);
                    addSlotToGateQueues(lSlots, slot, gates);
                    lIndex++;
                }
            }
        }

        return new ParkingLot(sSlots, mSlots, lSlots, pricingStrategy, slotRegistry);
    }

    private static Map<Gate, PriorityQueue<Slot>> initializeGateSlotQueues(List<Gate> gates) {
        Map<Gate, PriorityQueue<Slot>> queues = new HashMap<>();
        for (Gate gate : gates) {
            queues.put(gate, new PriorityQueue<>(Comparator.comparingInt(s -> s.distanceFrom(gate))));
        }
        return queues;
    }

    private static Slot createSlotS(
            int level,
            int index,
            List<Gate> gates,
            Map<Gate, Map<String, Integer>> gateSlotDistances,
            double pricePerHour
    ) {
        String slotId = formatSlotId(level, SlotType.S, index);
        Map<Gate, Integer> distances = buildGateDistancesMap(slotId, gates, gateSlotDistances);
        return new SlotS(slotId, level, distances, pricePerHour);
    }

    private static Slot createSlotM(
            int level,
            int index,
            List<Gate> gates,
            Map<Gate, Map<String, Integer>> gateSlotDistances,
            double pricePerHour
    ) {
        String slotId = formatSlotId(level, SlotType.M, index);
        Map<Gate, Integer> distances = buildGateDistancesMap(slotId, gates, gateSlotDistances);
        return new SlotM(slotId, level, distances, pricePerHour);
    }

    private static Slot createSlotL(
            int level,
            int index,
            List<Gate> gates,
            Map<Gate, Map<String, Integer>> gateSlotDistances,
            double pricePerHour
    ) {
        String slotId = formatSlotId(level, SlotType.L, index);
        Map<Gate, Integer> distances = buildGateDistancesMap(slotId, gates, gateSlotDistances);
        return new SlotL(slotId, level, distances, pricePerHour);
    }

    private static String formatSlotId(int level, SlotType type, int index) {
        return "L" + level + "-" + type + "-" + index;
    }

    private static Map<Gate, Integer> buildGateDistancesMap(
            String slotId,
            List<Gate> gates,
            Map<Gate, Map<String, Integer>> gateSlotDistances
    ) {
        Map<Gate, Integer> distances = new HashMap<>();
        for (Gate gate : gates) {
            Map<String, Integer> slotDistanceMap = gateSlotDistances.get(gate);
            int distance = slotDistanceMap != null ? slotDistanceMap.getOrDefault(slotId, 0) : 0;
            distances.put(gate, distance);
        }
        return distances;
    }

    private static void addSlotToGateQueues(Map<Gate, PriorityQueue<Slot>> gateQueues, Slot slot, List<Gate> gates) {
        for (Gate gate : gates) {
            gateQueues.get(gate).offer(slot);
        }
    }
}
