package com.example.project_iot.service;

import com.example.project_iot.models.CalendarEvent;
import com.example.project_iot.models.ResponceRoute;
import com.example.project_iot.repo.DevelopDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class CalculateRouterService {
    @Autowired
    RestService restService;

    public CalendarEvent getNearNotification(int userId) {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//должна быть работы только с карточками юзера

        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();

//        System.out.println(now);
//        System.out.println( fakeDB.alarms.get(0).getTimestamp());
        for (CalendarEvent temp: DevelopDB.calendarEvents
             ) {
            System.out.println(temp);


        }

        CalendarEvent findCalendarEvent = DevelopDB.calendarEvents.stream().
                filter(x -> (x.getDays()[((dayOfWeek-2)%7+7)%7] && x.getTimestamp()> now && x.isSelected()))
                .min(Comparator.comparing(CalendarEvent::getTimestamp))
                .orElse(null);

        if (findCalendarEvent !=null) return findCalendarEvent;


            for (int i = dayOfWeek+1; i<=dayOfWeek+7; i++){
                final int numberObWeek = (i -2)%7;
                findCalendarEvent = DevelopDB.calendarEvents.stream().filter(x -> (x.getDays()[numberObWeek]&& x.isSelected()))
                        .min(Comparator.comparing(CalendarEvent::getTimestamp))
                        .orElse(null);
                if (findCalendarEvent !=null) return findCalendarEvent;
    }
        return null;
    }

    private static int getWeather(double latitude, double longitude) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url  = "https://api.openweathermap.org/data/2.5/onecall?lat=30&lon=30&appid=bdfddada1ad52adf4f1786c07848d0ef";


        return 0;
    }


    public long getNotificationMode(CalendarEvent calendarEvent, int dayOfWeek, int now, String mode){
        //Выяснять время машрута
        now += 3600*3%(3600*24);
        long notification = -1;
        String answer = restService.getPostsPlainJSON(calendarEvent.getGeo(),mode);
        long seconds_route = getSeconds(answer);
        long upper = calendarEvent.getTimestamp();
        if((calendarEvent.getDays()[(dayOfWeek-2+7)%7])) {
            notification = (upper - (seconds_route + now))/60;
            if (notification<0) notification = 0;
        }
        return notification;
    }


    private long getSeconds(String str){
        String mass[] = str.split(" ");

        long seconds_route = 0;
        for (int i = 0; i < mass.length ; i++) {
            switch (mass[i]){
                case "day","days":
                    seconds_route += 86400 * Integer.parseInt(mass[i-1]);
                    break;
                case "hour","hours":
                    seconds_route += 3600 * Integer.parseInt(mass[i-1]);
                    break;
                case "min","mins":
                    seconds_route += 60 * Integer.parseInt(mass[i-1]);
                    break;
            }
        }
        return seconds_route;
    }

    public ResponceRoute responceForNucleo(int userId) throws NoSuchElementException{
        CalendarEvent calendarEvent = getNearNotification(userId);
        if(calendarEvent == null) throw new NoSuchElementException("Нет таких элементов");

        ResponceRoute responceRoute = new ResponceRoute();

        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        responceRoute.setDesc(calendarEvent.getDesc());
        responceRoute.setWalking(getNotificationMode(calendarEvent, dayOfWeek,now,"walking"));
        responceRoute.setTransit(getNotificationMode(calendarEvent, dayOfWeek,now,"transit"));
        responceRoute.setDriving(getNotificationMode(calendarEvent, dayOfWeek,now,"driving"));
        responceRoute.setIdAlarm(calendarEvent.getId());
        System.out.println(responceRoute);

        return responceRoute;
    }

    private String mapper(String form){
        if(form.equals("машиной")
                || form.equals("на машине")
                || form.equals("машина")) return "driving";

        if(form.equals("пешком")) return "walking";

        if(form.equals("публичным транспортом")
                || form.equals("маршруткой")
                || form.equals("общественным транспортом")) return "transit";
        return "walking";
    }

    public long responceForAlice(String mode) throws NoSuchElementException{
        CalendarEvent calendarEvent = getNearNotification(3);
        if(calendarEvent == null) throw new NoSuchElementException("Нет таких элементов");
        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        long g = getNotificationMode(calendarEvent, dayOfWeek,now,
                mapper(mode).toString());
        return g;

    }

}
