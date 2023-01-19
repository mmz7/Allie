package com.zhangmegan.allie;

public class DataEntry {
    int month, day, year, hour, min;
    String type, entry;

    public DataEntry(int month, int day, int year, int hour, int min, String type, String entry) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.type = type;
        this.entry = entry;
    }
}
