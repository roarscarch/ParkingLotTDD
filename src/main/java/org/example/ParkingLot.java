package org.example;

import java.time.LocalDateTime;
import java.util.*;

public class ParkingLot {
    private final String lotId;
    private final int capacity;
    private int carsParked;
    private final Map<String, Car> parkedCars;
    private final List<ParkingLotObserver> observers;
    private final Map<String, Integer> carLocations;
    private final Map<String, LocalDateTime> parkingTimes;
    private boolean wasFull;
    private final boolean[] parkingSpaces;
    private final boolean[] handicapParkingSpaces;
    private final Map<String, String> attendantRecords;
    private final Map<Integer, String> parkingRows;

    public ParkingLot(String lotId, int capacity, int handicapSpaces) {
        this.lotId = lotId;
        this.capacity = capacity;
        this.carsParked = 0;
        this.parkedCars = new HashMap<>();
        this.observers = new ArrayList<>();
        this.carLocations = new HashMap<>();
        this.parkingTimes = new HashMap<>();
        this.wasFull = false;
        this.parkingSpaces = new boolean[capacity];
        this.handicapParkingSpaces = new boolean[handicapSpaces];
        this.attendantRecords = new HashMap<>();
        this.parkingRows = new HashMap<>();
    }

    public int getCarsParked() {
        return carsParked;
    }

    public void addObserver(ParkingLotObserver observer) {
        observers.add(observer);
    }

    private void notifyFull() {
        observers.forEach(ParkingLotObserver::onParkingLotFull);
    }

    private void notifyAvailable() {
        observers.forEach(ParkingLotObserver::onParkingLotAvailable);
    }

    public boolean parkCar(Car car, String attendantName) {
        if (!isFull()) {
            int spaceNumber = getNextAvailableSpace();
            if (spaceNumber != -1) {
                carsParked++;
                parkedCars.put(car.getLicensePlate(), car);
                parkingTimes.put(car.getLicensePlate(), LocalDateTime.now());
                carLocations.put(car.getLicensePlate(), spaceNumber);
                parkingSpaces[spaceNumber] = true;
                System.out.println("Car " + car.getLicensePlate() + " parked at space " + spaceNumber + ".");
                attendantRecords.put(car.getLicensePlate(), attendantName);
                parkingRows.put(spaceNumber, determineRowForSpace(spaceNumber));
                return true;
            }
        }
        System.out.println("Parking lot is full. Cannot park " + car.getLicensePlate());
        return false;
    }

    public LocalDateTime getParkingTime(String licensePlate) {
        return parkingTimes.get(licensePlate);
    }

    public boolean unparkCar(String licensePlate) {
        Car car = parkedCars.remove(licensePlate);
        if (car != null) {
            Integer spaceNumber = carLocations.remove(licensePlate);
            if (spaceNumber != null) {
                parkingSpaces[spaceNumber] = false;
            }
            carsParked--;
            System.out.println("Car " + car.getLicensePlate() + " unparked. " +
                    (capacity - carsParked) + " spots available now.");
            parkingTimes.remove(licensePlate);

            if (wasFull && !isFull()) {
                System.out.println("Parking lot has space available now.");
                wasFull = false;
                notifyAvailable();
            }
            return true;
        } else {
            System.out.println("Car with license plate " + licensePlate + " not found.");
            return false;
        }
    }

    public boolean isFull() {
        return carsParked == capacity;
    }

    public String findCarLocation(String licensePlate) {
        Integer spaceNumber = carLocations.get(licensePlate);
        return (spaceNumber != null) ?
                "Your car is parked at space " + spaceNumber + "." :
                "Car not found in the parking lot.";
    }

    public String getLotId() {
        return lotId;
    }

    public boolean isCarParked(String licensePlate) {
        return parkedCars.containsKey(licensePlate);
    }

    public int getNextAvailableSpace() {
        for (int i = 0; i < capacity; i++) {
            if (!parkingSpaces[i]) {
                return i;
            }
        }
        return -1;
    }

    public int getNearestHandicapSpace() {
        for (int i = 0; i < handicapParkingSpaces.length; i++) {
            if (!handicapParkingSpaces[i]) {
                return i;
            }
        }
        return -1;
    }

    public int getAvailableSpaces() {
        return capacity - carsParked;
    }

    public List<String> findCarsByColor(String color) {
        List<String> locations = new ArrayList<>();
        for (Map.Entry<String, Car> entry : parkedCars.entrySet()) {
            Car car = entry.getValue();
            if (color.equalsIgnoreCase(car.getColor())) {
                locations.add("Lot " + lotId + " Space " + carLocations.get(entry.getKey()));
            }
        }
        return locations;
    }

    public List<String> findCarsByMakeAndColor(String make, String color) {
        List<String> details = new ArrayList<>();
        for (Map.Entry<String, Car> entry : parkedCars.entrySet()) {
            Car car = entry.getValue();
            if (make.equalsIgnoreCase(car.getMake()) && color.equalsIgnoreCase(car.getColor())) {
                String attendantName = attendantRecords.get(entry.getKey());
                details.add("Lot " + lotId + " Space " + carLocations.get(entry.getKey()) +
                        ", Plate: " + entry.getKey() + ", Attendant: " + attendantName);
            }
        }
        return details;
    }

    public List<String> findCarsByMake(String make) {
        List<String> carDetails = new ArrayList<>();
        for (Map.Entry<String, Car> entry : parkedCars.entrySet()) {
            Car car = entry.getValue();
            if (make.equalsIgnoreCase(car.getMake())) {
                carDetails.add("Lot " + lotId + " Space " + carLocations.get(entry.getKey()) + ", Plate: " + entry.getKey());
            }
        }
        return carDetails;
    }

    public Map<String, LocalDateTime> getParkingTimes() {
        return parkingTimes;
    }

    private String determineRowForSpace(int spaceNumber) {
        return (spaceNumber % 2 == 0) ? "D" : "B";
    }

    public List<String> findSmallHandicapCarsInRows(String row1, String row2) {
        List<String> details = new ArrayList<>();
        for (Map.Entry<String, Car> entry : parkedCars.entrySet()) {
            Car car = entry.getValue();
            if ("Small".equalsIgnoreCase(car.getSize()) && car.isHandicap()) {
                Integer spaceNumber = carLocations.get(entry.getKey());
                String parkedRow = parkingRows.get(spaceNumber);
                if (row1.equals(parkedRow) || row2.equals(parkedRow)) {
                    details.add("Lot " + lotId + " Space " + spaceNumber + ", Plate: " + entry.getKey());
                }
            }
        }
        return details;
    }

    public List<String> getAllParkedCarsInfo() {
        List<String> carDetails = new ArrayList<>();
        for (Map.Entry<String, Car> entry : parkedCars.entrySet()) {
            Car car = entry.getValue();
            String info = "Plate: " + entry.getKey() + ", Make: " + car.getMake() +
                    ", Color: " + car.getColor() + ", Size: " + car.getSize();
            carDetails.add(info);
        }
        return carDetails;
    }
}
