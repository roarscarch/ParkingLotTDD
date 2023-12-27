package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParkingLotManager {
    private final List<ParkingLot> parkingLots;

    public ParkingLotManager(List<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }

    public ParkingLot findLeastOccupiedLot() {
        return parkingLots.stream()
                .min(Comparator.comparingInt(ParkingLot::getCarsParked))
                .orElse(null);
    }

    public List<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public ParkingLot findMostVacantLot() {
        return parkingLots.stream()
                .max(Comparator.comparingInt(ParkingLot::getAvailableSpaces))
                .orElse(null);
    }

    public List<String> getLocationsOfWhiteCars() {
        List<String> whiteCarLocations = new ArrayList<>();
        parkingLots.forEach(lot -> whiteCarLocations.addAll(lot.findCarsByColor("white")));
        return whiteCarLocations;
    }

    public List<String> getDetailsOfBlueToyotas() {
        List<String> carDetails = new ArrayList<>();
        parkingLots.forEach(lot -> carDetails.addAll(lot.findCarsByMakeAndColor("toyota", "blue")));
        return carDetails;
    }

    public List<String> getDetailsOfBMWCars() {
        List<String> bmwCarDetails = new ArrayList<>();
        parkingLots.forEach(lot -> bmwCarDetails.addAll(lot.findCarsByMake("BMW")));
        return bmwCarDetails;
    }

    public List<String> getCarsParkedInLast30Minutes() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<String> recentCarDetails = new ArrayList<>();
        parkingLots.forEach(lot -> {
            lot.getParkingTimes().entrySet().stream()
                    .filter(entry -> entry.getValue().isAfter(thirtyMinutesAgo))
                    .map(entry -> "Lot " + lot.getLotId() + " Plate: " + entry.getKey())
                    .forEach(recentCarDetails::add);
        });
        return recentCarDetails;
    }

    public List<String> getSmallHandicapCarsInRowsBOrD() {
        List<String> carDetails = new ArrayList<>();
        parkingLots.forEach(lot -> carDetails.addAll(lot.findSmallHandicapCarsInRows("B", "D")));
        return carDetails;
    }
}
