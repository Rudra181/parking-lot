package com.parkinglot;

import java.time.LocalTime;

public class HourlyPricing implements PricingStrategy {
    @Override
    public double calculate(LocalTime entryTime, LocalTime exitTime, double pricePerHour) {
        long minutesDifference = java.time.temporal.ChronoUnit.MINUTES.between(entryTime, exitTime);
        long hoursCeiling = (minutesDifference + 59) / 60;
        return hoursCeiling * pricePerHour;
    }
}
