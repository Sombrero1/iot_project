package com.example.project_iot.repo;

import com.example.project_iot.models.CalendarEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public class DevelopDB {
    public static ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
    public static int id = 11;
}
