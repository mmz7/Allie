package com.zhangmegan.allie;

public class DataEntry {
    int month, day, year, hour, minute;
    String type, entry;

    public DataEntry(int month, int day, int year, int hour, int minute, String type, String entry) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.type = type;
        this.entry = entry;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    public void setDay(int day){
        this.day = day;
    }
    public void setYear(int year){
        this.year = year;
    }
    public void setHour(int hour){
        this.hour = hour;
    }
    public void setMinute(int minute){
        this.minute = minute;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setEntry(String entry){
        this.entry = entry;
    }

    public int getMonth(){
        return this.month;
    }
    public int getDay(){
        return this.day;
    }
    public int getYear(){
        return this.year;
    }
    public int getHour(){
        return this.hour;
    }
    public int getMinute(){
        return this.minute;
    }
    public String getType(){
        return this.type;
    }
    public String getEntry(){
        return this.entry;
    }

}
