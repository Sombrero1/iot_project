package com.example.project_iot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Alarm {
    private String time;
    private int id;
    private Double[] geo;
    private static SimpleDateFormat format;
    private boolean []days; //массив на 7 элементов
    private boolean selected;

    public Alarm(){}

    public Alarm(String time, int id, Double[] geo, boolean[] days, boolean selected) {
        this.time = time;
        this.id = id;
        this.geo = geo;
        this.days = days;
        this.selected = selected;
    }

    static {
        format = new SimpleDateFormat("HH:mm");
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
        return "Alarm{" +
                "time='" + time + '\'' +
                ", id=" + id +
                ", geo=" + Arrays.toString(geo) +
                ", days=" + Arrays.toString(days) +
                ", selected=" + selected +
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
        Alarm alarm = (Alarm) o;
        return id == alarm.id &&
                selected == alarm.selected &&
                Objects.equals(time, alarm.time) &&
                Arrays.equals(geo, alarm.geo) &&
                Arrays.equals(days, alarm.days);
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
