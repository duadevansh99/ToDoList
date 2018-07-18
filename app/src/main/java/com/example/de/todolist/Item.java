package com.example.de.todolist;

public class Item {
    String item;
    String description;
    int epYear,epMonth,epDay,epHour,epMinute;


    private long id;
    public Item(String item, String description, String date, String time, int epYear, int epMonth, int epDay, int epHour, int epMinute) {
        this.item = item;
        this.description = description;
        this.date=date;
        this.time=time;
        this.epYear=epYear;
        this.epDay=epDay;
        this.epMonth=epMonth;
        this.epHour=epHour;
        this.epMinute=epMinute;
    }

    private String date,time;

    public int getEpYear() { return epYear; }

    public void setEpYear(int epYear) { this.epYear = epYear; }

    public int getEpMonth() { return epMonth; }

    public void setEpMonth(int epMonth) { this.epMonth = epMonth; }

    public int getEpDay() { return epDay; }

    public void setEpDay(int epDay) { this.epDay = epDay; }

    public int getEpHour() { return epHour; }

    public void setEpHour(int epHour) { this.epHour = epHour; }

    public int getEpMinute() { return epMinute; }

    public void setEpMinute(int epMinute) { this.epMinute = epMinute; }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
