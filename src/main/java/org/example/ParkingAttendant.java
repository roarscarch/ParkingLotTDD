package org.example;

public class ParkingAttendant {
    private String name;
    private ParkingLot parkingLot; 
    private ParkingLotManager lotManager; 

    public ParkingAttendant(ParkingLot parkingLot, String name) {
        this.parkingLot = parkingLot;
        this.name = name;
    }

    public ParkingAttendant(ParkingLotManager lotManager, String name) {
        this.lotManager = lotManager;
        this.name = name;
    }

    public boolean parkCar(Car car) {
        ParkingLot targetLot = (lotManager != null) ? lotManager.findLeastOccupiedLot() : parkingLot;
        return (targetLot != null) && targetLot.parkCar(car, this.name);
    }

    public String findCarLocation(String licensePlate) {
        ParkingLot targetLot = (lotManager != null) ? findCarInLots(licensePlate) : parkingLot;
        return (targetLot != null) ? targetLot.findCarLocation(licensePlate) : "Car not found.";
    }

    public boolean unparkCar(String licensePlate) {
        ParkingLot targetLot = (lotManager != null) ? findCarInLots(licensePlate) : parkingLot;
        return (targetLot != null) && targetLot.unparkCar(licensePlate);
    }

    public boolean parkHandicapCar(Car car) {
        ParkingLot targetLot = (lotManager != null) ? lotManager.findLeastOccupiedLot() : parkingLot;
        return (targetLot != null) && targetLot.parkCar(car, this.name);
    }

    public boolean parkLargeCar(Car car) {
        ParkingLot targetLot = (lotManager != null) ? lotManager.findMostVacantLot() : parkingLot;
        return (targetLot != null) && targetLot.parkCar(car, this.name);
    }

    private ParkingLot findCarInLots(String licensePlate) {
        if (lotManager != null) {
            for (ParkingLot lot : lotManager.getParkingLots()) {
                if (lot.isCarParked(licensePlate)) {
                    return lot;
                }
            }
        }
        return null;
    }
}
