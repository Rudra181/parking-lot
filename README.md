# Parking Lot System

A low-level design implementation of a multi-level parking lot system in Java.

## Features

- **Multi-level parking**: Configurable number of levels with different slot types
- **Slot Types**: Small (S) for two-wheelers, Medium (M) for mid-size cars, Large (L) for large vehicles
- **Vehicle Types**: TWO_WHEELER, FOUR_M (mid-size), FOUR_L (large)
- **Multiple Gates**: Different entry/exit points with distance-based slot allocation
- **Pricing Strategy**: Flexible pricing with hourly billing (rounds up to nearest hour)
- **Slot Availability**: Priority queue-based nearest slot selection from each gate
- **Ticket System**: Generate and validate tickets for vehicle entry/exit

## Architecture

### Core Classes

- **Gate**: Represents entry/exit points with unique IDs
- **VehicleDetails**: Immutable data class holding vehicle information
- **Ticket**: Generated on successful parking, used for exit validation
- **ParkingLot**: Main system managing all operations
- **Slot**: Abstract base for different slot sizes with configurable pricing
- **SlotS, SlotM, SlotL**: Concrete slot implementations

### Pricing

- **PricingStrategy**: Interface for flexible pricing implementations
- **HourlyPricing**: Rounds parking duration to nearest hour and multiplies by hourly rate

### Factory Pattern

- **ParkingLotFactory**: Builds complete parking lot with all required infrastructure
- Generates slot IDs in format: `L{level}-{type}-{index}`
- Sets up priority queues for each gate-slottype combination

## Validation

- Vehicle type compatibility with slot size
- Ticket validation during exit
- Slot availability checks

## Exception Handling

- **NoSlotAvailableException**: Thrown when no slots available for requested type
- **IncompatibleVehicleException**: Thrown when vehicle doesn't fit slot type
- **InvalidTicketException**: Thrown when ticket is invalid or slot not found

## Usage

```java
ParkingLot lot = ParkingLotFactory.create(
    2,                          // levels
    levelSlotMapping,           // slot configuration per level
    gates,                      // list of gates
    gateSlotDistances,         // distances from each gate to slots
    pricesPerHour,             // pricing per slot type
    new HourlyPricing()        // pricing strategy
);

Ticket ticket = lot.park(vehicleDetails, entryTime, gate, SlotType.M);
int fee = lot.exit(ticket, exitTime);
Map<SlotType, Integer> status = lot.status();
```

## Compilation

```bash
javac -d bin src/com/parkinglot/*.java
```

## Execution

```bash
java -cp bin com.parkinglot.Main
```

## Package Structure

```
com.parkinglot/
├── Gate.java
├── VehicleType.java
├── VehicleDetails.java
├── SlotType.java
├── Slot.java
├── SlotS.java
├── SlotM.java
├── SlotL.java
├── Ticket.java
├── PricingStrategy.java
├── HourlyPricing.java
├── NoSlotAvailableException.java
├── IncompatibleVehicleException.java
├── InvalidTicketException.java
├── ParkingLot.java
├── ParkingLotFactory.java
└── Main.java
```
