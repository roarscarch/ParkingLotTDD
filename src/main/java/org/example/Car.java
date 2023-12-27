package org.example;

public class Car {
    private final String color;
    private final String make;
    private final String size;
    private final boolean isHandicap;
    private final String licensePlate;

    public Car(String licensePlate, String color, String make, String size, boolean isHandicap) {
        this.licensePlate = licensePlate;
        this.color = color;
        this.make = make;
        this.size = size;
        this.isHandicap = isHandicap;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColor() {
        return color;
    }

    public String getMake() {
        return make;
    }

    public String getSize() {
        return size;
    }

    public boolean isHandicap() {
        return isHandicap;
    }

    @Override
    public String toString() {
        return "Car{" +
                "licensePlate='" + licensePlate + '\'' +
                ", color='" + color + '\'' +
                ", make='" + make + '\'' +
                ", size='" + size + '\'' +
                ", isHandicap=" + isHandicap +
                '}';
    }
}
