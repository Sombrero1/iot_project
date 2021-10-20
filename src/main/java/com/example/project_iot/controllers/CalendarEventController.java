package com.example.project_iot.controllers;


import com.example.project_iot.service.RestService;
import com.example.project_iot.models.CalendarEvent;
import com.example.project_iot.repo.DevelopDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/events")
public class CalendarEventController { //#todo обработать входные данные
    @Autowired
    RestService restService;

    @GetMapping()
    public List getCalendarEvent(){
        System.out.println("get :" + DevelopDB.calendarEvents.toString());
        return DevelopDB.calendarEvents;
    }

    @PostMapping()
    public int addCalendarEvent(@RequestBody CalendarEvent calendarEvent){
        System.out.print("post : ");
        System.out.println(calendarEvent);
        calendarEvent.setId(DevelopDB.id++);
        DevelopDB.calendarEvents.add(calendarEvent);
        return  calendarEvent.getId();
    }

    @PutMapping("/{id}")
    public void putCalendarEvent(@PathVariable int id,@RequestBody CalendarEvent calendarEvent){
        System.out.println("put : "+id +" "+ calendarEvent);
        for (CalendarEvent temp: DevelopDB.calendarEvents
             ) {
            if(temp.getId() == id){
                temp.setTime(calendarEvent.getTime());
                temp.setGeo(calendarEvent.getGeo());
                temp.setSelected(calendarEvent.isSelected());
                temp.setDays(calendarEvent.getDays());
                return;
            }

        }
        DevelopDB.calendarEvents.add(calendarEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteCalendarEvent(@PathVariable int id){
        System.out.print("delete : " + id);
        Iterator<CalendarEvent> iterator =  DevelopDB.calendarEvents.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getId()==id){
                iterator.remove();
                break;
            };
        }
    }
}
