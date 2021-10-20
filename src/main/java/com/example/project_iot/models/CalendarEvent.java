package com.example.project_iot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class CalendarEvent {
    private String time;
    private int id;
    private Double[] geo;
    private static SimpleDateFormat format;
    private boolean []days;
    private boolean selected;
    private String desc;

    public CalendarEvent(){}

    public CalendarEvent(String time, int id, Double[] geo, boolean[] days, boolean selected, String desc) {
        this.time = time;
        this.id = id;
        this.geo = geo;
        this.days = days;
        this.selected = selected;
        this.desc = desc;
    }

    static {
        format = new SimpleDateFormat("HH:mm");
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public final Double[] getGeo() {
        return geo;
    }

    public void setGeo(Double[] geo) {
        this.geo = geo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public long getTimestamp(){
        try {
            Date date = format.parse(time);
            return date.getHours()*3600+date.getMinutes()*60+date.getSeconds();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return 0;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "time='" + time + '\'' +
                ", id=" + id +
                ", geo=" + Arrays.toString(geo) +
                ", days=" + Arrays.toString(days) +
                ", selected=" + selected +
                ", desc='" + desc + '\'' +
                '}';
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvent calendarEvent = (CalendarEvent) o;
        return id == calendarEvent.id &&
                selected == calendarEvent.selected &&
                Objects.equals(time, calendarEvent.time) &&
                Arrays.equals(geo, calendarEvent.geo) &&
                Arrays.equals(days, calendarEvent.days);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(time, id, selected);
        result = 31 * result + Arrays.hashCode(geo);
        result = 31 * result + Arrays.hashCode(days);
        return result;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }
}
