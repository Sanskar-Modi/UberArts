package com.example.sanskarmodi.uberarts.model;

public class Order {

    private String classType;
    private String time;
    private int price;

    public Order(String classType, String time, int price) {
        this.classType = classType;
        this.time = time;
        this.price = price;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
