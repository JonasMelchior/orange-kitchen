package com.application.entity.kitchen.store;

public class DailyRevenue {
    int dayOfMonth;
    double revenue;

    public DailyRevenue(int dayOfMonth, double revenue) {
        this.dayOfMonth = dayOfMonth;
        this.revenue = revenue;
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
