package com.example.project_iot.controllers;


import com.example.project_iot.Notification;
import com.example.project_iot.RestService;
import com.example.project_iot.models.Alarm;
import com.example.project_iot.repo.FakeDB;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlarmController {
    @Autowired
    RestService restService;

    @PostMapping("/put_clock")
    public Alarm putAlarm(@RequestBody Alarm alarm){

        System.out.println(alarm.getName() +" "+ alarm.getId() +" "+alarm.getGeo()[0]+" "+alarm.getGeo()[1]+" "+alarm.getGeo()[2]+" "+alarm.getGeo()[3]+alarm.getDays()[0]);
        System.out.println(alarm);
        FakeDB.alarms.clear();
        FakeDB.alarms.add(alarm);

        return alarm;
    }
}
