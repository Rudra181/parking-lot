package com.parkinglot;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Gate gate1 = new Gate(1);
        Gate gate2 = new Gate(2);
        Gate gate3 = new Gate(3);
        List<Gate> gates = Arrays.asList(gate1, gate2, gate3);

        Map<Integer, Map<SlotType, Integer>> levelSlotMapping = new HashMap<>();
        Map<SlotType, Integer> level0Slots = new HashMap<>();
        level0Slots.put(SlotType.S, 2);
        level0Slots.put(SlotType.M, 2);
        level0Slots.put(SlotType.L, 2);
        levelSlotMapping.put(0, level0Slots);

        Map<SlotType, Integer> level1Slots = new HashMap<>();
        level1Slots.put(SlotType.S, 2);
        level1Slots.put(SlotType.M, 2);
        level1Slots.put(SlotType.L, 2);
        levelSlotMapping.put(1, level1Slots);

        Map<Gate, Map<String, Integer>> gateSlotDistances = new HashMap<>();

        Map<String, Integer> gate1Distances = new HashMap<>();
        int distanceCounter = 0;
        for (int level = 0; level < 2; level++) {
            for (SlotType type : SlotType.values()) {
                for (int i = 0; i < levelSlotMapping.get(level).getOrDefault(type, 0); i++) {
                    String slotId = "L" + level + "-" + type + "-" + i;
                    gate1Distances.put(slotId, distanceCounter++);
                }
            }
        }
        gateSlotDistances.put(gate1, gate1Distances);

        Map<String, Integer> gate2Distances = new HashMap<>();
        distanceCounter = 0;
        for (int level = 0; level < 2; level++) {
            for (SlotType type : SlotType.values()) {
                for (int i = 0; i < levelSlotMapping.get(level).getOrDefault(type, 0); i++) {
                    String slotId = "L" + level + "-" + type + "-" + i;
                    gate2Distances.put(slotId, distanceCounter += 2);
                }
            }
        }
        gateSlotDistances.put(gate2, gate2Distances);

        Map<String, Integer> gate3Distances = new HashMap<>();
        distanceCounter = 0;
        for (int level = 0; level < 2; level++) {
            for (SlotType type : SlotType.values()) {
                for (int i = 0; i < levelSlotMapping.get(level).getOrDefault(type, 0); i++) {
                    String slotId = "L" + level + "-" + type + "-" + i;
                    gate3Distances.put(slotId, distanceCounter += 3);
                }
            }
        }
        gateSlotDistances.put(gate3, gate3Distances);

        Map<SlotType, Double> pricesPerHour = new HashMap<>();
        pricesPerHour.put(SlotType.S, 50.0);
        pricesPerHour.put(SlotType.M, 75.0);
        pricesPerHour.put(SlotType.L, 100.0);

        PricingStrategy pricingStrategy = new HourlyPricing();

        ParkingLot parkingLot = ParkingLotFactory.create(
                2,
                levelSlotMapping,
                gates,
                gateSlotDistances,
                pricesPerHour,
                pricingStrategy
        );

        System.out.println("Parking Lot System Initialized");
        System.out.println("Initial Status: " + parkingLot.status());

        VehicleDetails bike = new VehicleDetails("MH01AB1234", VehicleType.TWO_WHEELER, "John Doe");
        LocalTime bikeEntryTime = LocalTime.of(10, 0);
        Ticket bikeTicket = parkingLot.park(bike, bikeEntryTime, gate1, SlotType.S);
        System.out.println("\nBike parked in slot: " + bikeTicket.getSlotId());

        VehicleDetails car = new VehicleDetails("MH02CD5678", VehicleType.FOUR_M, "Jane Smith");
        LocalTime carEntryTime = LocalTime.of(10, 15);
        Ticket carTicket = parkingLot.park(car, carEntryTime, gate2, SlotType.M);
        System.out.println("Car parked in slot: " + carTicket.getSlotId());

        VehicleDetails van = new VehicleDetails("MH03EF9012", VehicleType.FOUR_L, "Bob Johnson");
        LocalTime vanEntryTime = LocalTime.of(10, 30);
        Ticket vanTicket = parkingLot.park(van, vanEntryTime, gate3, SlotType.L);
        System.out.println("Van parked in slot: " + vanTicket.getSlotId());

        System.out.println("\nStatus after parking 3 vehicles: " + parkingLot.status());

        LocalTime bikeExitTime = LocalTime.of(11, 45);
        int bikeFee = parkingLot.exit(bikeTicket, bikeExitTime);
        System.out.println("\nBike exit fee: " + bikeFee);

        LocalTime carExitTime = LocalTime.of(12, 20);
        int carFee = parkingLot.exit(carTicket, carExitTime);
        System.out.println("Car exit fee: " + carFee);

        System.out.println("\nStatus after 2 exits: " + parkingLot.status());

        VehicleDetails bike2 = new VehicleDetails("MH04GH3456", VehicleType.TWO_WHEELER, "Alice Wilson");
        LocalTime bike2EntryTime = LocalTime.of(13, 0);
        Ticket bike2Ticket = parkingLot.park(bike2, bike2EntryTime, gate1, SlotType.S);
        System.out.println("\nSecond bike parked in slot: " + bike2Ticket.getSlotId());

        System.out.println("Final Status: " + parkingLot.status());
    }
}
