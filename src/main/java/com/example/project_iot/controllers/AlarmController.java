package com.example.project_iot.controllers;


import com.example.project_iot.RestService;
import com.example.project_iot.models.Alarm;
import com.example.project_iot.repo.FakeDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/clocks")
public class AlarmController {
    @Autowired
    RestService restService;

    @GetMapping()
    public List getAlarm(){
        System.out.println("get :" + FakeDB.alarms.toString());
        return FakeDB.alarms;
    }

    @PostMapping()
    public int addAlarm(@RequestBody Alarm alarm){
        System.out.print("post : ");
        System.out.println(alarm);
        alarm.setId(FakeDB.id++);
        FakeDB.alarms.add(alarm);
        return  alarm.getId();
    }

    @PutMapping("/{id}")
    public void putAlarm(@PathVariable int id,@RequestBody Alarm alarm){
        System.out.println("put : "+id +" "+ alarm);
        for (Alarm temp: FakeDB.alarms
             ) {
            if(temp.getId() == id){
                temp.setTime(alarm.getTime());
                temp.setGeo(alarm.getGeo());
                temp.setSelected(alarm.isSelected());
                temp.setDays(alarm.getDays());
                return;
            }

        }
        FakeDB.alarms.add(alarm);
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
