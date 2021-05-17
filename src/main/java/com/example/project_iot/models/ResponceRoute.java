package com.example.project_iot.models;

import lombok.Data;

@Data
public class ResponceRoute {
    private int walking;
    private int transit;
    private int driving;


    @Override
    public String toString() {
        return "ResponceRoute{" +
                "walking=" + walking +
                ", transit=" + transit +
                ", driving=" + driving +
                '}';
    }
}
