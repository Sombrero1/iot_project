package com.example.project_iot.repo;

import com.example.project_iot.models.Alarm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public class FakeDB {
    public static ArrayList<Alarm> alarms = new ArrayList<>();
    public static int id = 11;
}
