package com.example.carrental.model;

public class Car {
    private int id;
    private String name;
    private String brand;
    private String model;
    private int year;
    private String color;
    private double pricePerDay;
    private String availability;

    private String imageUrl;


    public Car(int id, String name, String brand, String model, int year, String color, double pricePerDay, String availability, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.pricePerDay = pricePerDay;
        this.availability = availability;
        this.imageUrl = imageUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // toString method for easy display in spinner or list view
    @Override
    public String toString() {
        return brand + " " + model + " (" + year + "), Color: " + color + ", Availability: " + availability;
    }
}
