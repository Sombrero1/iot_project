package com.example.project_iot.controllers;


import com.example.project_iot.Notification;
import com.example.project_iot.RestService;
import com.example.project_iot.models.Alarm;
import com.example.project_iot.repo.FakeDB;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
@RequestMapping("/clocks")
public class AlarmController {
    @Autowired
    RestService restService;

    @PostMapping()
    public void addAlarm(@RequestBody Alarm alarm){
        System.out.print("post : ");
        System.out.println(alarm);
//        FakeDB.alarms.clear();
        FakeDB.alarms.add(alarm);
    }

    @PutMapping("/{id}")
    public void putAlarm(@PathVariable int id,@RequestBody Alarm alarm){
        System.out.println("put : "+id);
        System.out.println(alarm);
        for (Alarm temp: FakeDB.alarms
             ) {
            if(temp.getId() == id){
                temp.setName(alarm.getName());
                temp.setGeo(alarm.getGeo());
                temp.setSelected(alarm.isSelected());
                temp.setDays(alarm.getDays());
            }

        }


    }

    @DeleteMapping("/{id}")
    public void deleteAlarm(@PathVariable int id){
        System.out.print("delete : " + id);

        Iterator<Alarm> iterator =  FakeDB.alarms.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getId()==id){
                iterator.remove();
                break;
            };
        }
    }
}
