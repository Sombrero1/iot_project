package com.example.project_iot.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Alarm {
    private String name;
    private int id;
    private Double[] geo;
    private static SimpleDateFormat format;
    private boolean []days; //массив на 7 элементов
    private boolean selected;


    static {
        format = new SimpleDateFormat("HH:mm");
    }



    public final Double[] getGeo() {
        return geo;
    }

    public void setGeo(Double[] geo) {
        this.geo = geo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp(){
        try {
            Date date = format.parse(name);
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
                "name='" + name + '\'' +
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
                Objects.equals(name, alarm.name) &&
                Arrays.equals(geo, alarm.geo) &&
                Arrays.equals(days, alarm.days);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, id, selected);
        result = 31 * result + Arrays.hashCode(geo);
        result = 31 * result + Arrays.hashCode(days);
        return result;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }
}
