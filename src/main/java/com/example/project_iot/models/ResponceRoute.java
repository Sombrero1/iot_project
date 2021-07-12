package com.example.project_iot.models;

import lombok.Data;

@Data
public class ResponceRoute {
    private long walking;
    private long transit;
    private long driving;
    private int beep;//1 - 0
    private int idAlarm;


    @Override
    public String toString() {
        return "ResponceRoute{" +
                "walking=" + walking +
                ", transit=" + transit +
                ", driving=" + driving +
                '}';
    }
}
