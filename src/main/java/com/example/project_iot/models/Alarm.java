package com.example.project_iot.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Alarm {
    String name;
    int id;
    Double[] geo;
    private static SimpleDateFormat format;
    boolean []days; //массив на 7 элементов

    public Alarm() {
    }
    static {
        format = new SimpleDateFormat("HH:mm");
    }

    public Alarm(String name, int id, Double[] geo, boolean days[]) {
        this.name = name;
        this.id = id;
        this.geo = geo;
        this.days = days;
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
                '}';
    }
}
