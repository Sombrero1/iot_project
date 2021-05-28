package com.example.project_iot;

import com.example.project_iot.models.Alarm;
import com.example.project_iot.models.ResponceRoute;
import com.example.project_iot.repo.FakeDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class Notification {


    @Autowired
    RestService restService;


    private Alarm getNearNotification(int userId) {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//должна быть работы только с карточками юзера

        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();

//        System.out.println(now);
//        System.out.println( fakeDB.alarms.get(0).getTimestamp());
        for (Alarm temp:FakeDB.alarms
             ) {
            System.out.println(temp);


        }

        Alarm findAlarm = FakeDB.alarms.stream().
                filter(x -> (x.getDays()[(dayOfWeek-2)%7] && x.getTimestamp()> now && x.isSelected()))
                .min(Comparator.comparing(Alarm::getTimestamp))
                .orElse(null);

        if (findAlarm!=null) return  findAlarm;


            for (int i = dayOfWeek+1; i<=dayOfWeek+7; i++){
                final int numberObWeek = (i -2)%7;
                findAlarm = FakeDB.alarms.stream().filter(x -> (x.getDays()[numberObWeek]&& x.isSelected()))
                        .min(Comparator.comparing(Alarm::getTimestamp))
                        .orElse(null);
                if (findAlarm!=null) return  findAlarm;
    }
        return null;
    }


    private long getNotificationMode(Alarm alarm, int dayOfWeek, int now, String mode){
        //Выяснять время машрута
        long notification = -1;
        String answer = restService.getPostsPlainJSON(alarm.getGeo(),mode);//также другие режимы
        long seconds_route = getSeconds(answer);
        long upper = alarm.getTimestamp();
        if((alarm.getDays()[dayOfWeek-2])) {
            notification = (upper - (seconds_route + now))/60;//рассмотреть случай в 1:00
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
        Alarm alarm = getNearNotification(userId);
        if(alarm == null) throw new NoSuchElementException("Нет таких элементов");
        ResponceRoute responceRoute = new ResponceRoute();

        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


        responceRoute.setWalking(getNotificationMode(alarm, dayOfWeek,now,"walking"));
        responceRoute.setTransit(getNotificationMode(alarm, dayOfWeek,now,"transit"));
        responceRoute.setDriving(getNotificationMode(alarm, dayOfWeek,now,"driving"));
        System.out.println(responceRoute);

        return responceRoute;
    }

}
