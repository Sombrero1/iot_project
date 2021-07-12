package com.example.project_iot;

import com.example.project_iot.models.Alarm;
import com.example.project_iot.models.ResponceRoute;
import com.example.project_iot.repo.FakeDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class Notification {
    @Autowired
    RestService restService;


    public Alarm getNearNotification(int userId) {
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
                filter(x -> (x.getDays()[((dayOfWeek-2)%7+7)%7] && x.getTimestamp()> now && x.isSelected()))
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
    public static void main(String[] args) throws IOException, InterruptedException {
        getWeather(10,10);
    }

    private static int getWeather(double latitude, double longitude) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url  = "https://api.openweathermap.org/data/2.5/onecall?lat=30&lon=30&appid=bdfddada1ad52adf4f1786c07848d0ef";


        return 0;
    }


    public long getNotificationMode(Alarm alarm, int dayOfWeek, int now, String mode){
        //Выяснять время машрута
        now += 3600*3%(3600*24);
        long notification = -1;
        String answer = restService.getPostsPlainJSON(alarm.getGeo(),mode);//также другие режимы
        long seconds_route = getSeconds(answer);
        long upper = alarm.getTimestamp();
        if((alarm.getDays()[(dayOfWeek-2+7)%7])) {
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
        responceRoute.setIdAlarm(alarm.getId());
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
        Alarm alarm = getNearNotification(3);
        if(alarm == null) throw new NoSuchElementException("Нет таких элементов");
        Date date = new Date();
        int now = date.getHours()*3600+date.getMinutes()*60+date.getSeconds();
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        long g = getNotificationMode(alarm, dayOfWeek,now,
                mapper(mode).toString());
        return g;

    }

}
