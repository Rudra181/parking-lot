package com.parkinglot;

import java.time.LocalTime;

public interface PricingStrategy {
    double calculate(LocalTime entryTime, LocalTime exitTime, double pricePerHour);
}
