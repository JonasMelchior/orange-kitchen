package com.application.entity.kitchen.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyRevenue {
    private int dayOfMonth;
    private Map<String, Double> brandRevenues = new HashMap<>();
    private double revenue;

    private int roomNumber;

    public DailyRevenue(int dayOfMonth, double revenue) {
        this.dayOfMonth = dayOfMonth;
        this.revenue = revenue;
    }

    public DailyRevenue(int dayOfMonth, double revenue, int roomNumber) {
        this.dayOfMonth = dayOfMonth;
        this.revenue = revenue;
        this.roomNumber = roomNumber;
    }

    public DailyRevenue(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Map<String, Double> getBrandRevenues() {
        return brandRevenues;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setBrandRevenues(Map<String, Double> brandRevenues) {
        this.brandRevenues = brandRevenues;
    }

    public DailyRevenue() {

    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
